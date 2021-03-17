package com.edelivery.store.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import androidx.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.edelivery.store.parse.ParseContent;
import com.elluminati.edelivery.store.R;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by elluminati on 30-01-2017.
 */
public class ImageHelper {

    public static final String Tag = "ImageHelper";
    public static final int CHOOSE_PHOTO_FROM_GALLERY = 1;
    public static final int TAKE_PHOTO_FROM_CAMERA = 2;
    private Context context;
    private ParseContent parseContent;

    public ImageHelper(Context context) {
        parseContent = ParseContent.getParseContentInstance();
        this.context = context;
    }

    @Nullable
    public static File getFromMediaUriPfd(Context context, ContentResolver resolver, Uri uri) {
        if (uri == null) return null;

        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            ParcelFileDescriptor pfd = resolver.openFileDescriptor(uri, "r");
            FileDescriptor fd = pfd.getFileDescriptor();
            input = new FileInputStream(fd);

            String tempFilename = getTempFilename(context);
            output = new FileOutputStream(tempFilename);

            int read;
            byte[] bytes = new byte[4096];
            while ((read = input.read(bytes)) != -1) {
                output.write(bytes, 0, read);
            }
            return new File(tempFilename);
        } catch (IOException ignored) {
            // Nothing we can do
        } finally {
            closeSilently(input);
            closeSilently(output);
        }
        return null;
    }

    private static String getTempFilename(Context context) throws IOException {
        File outputDir = context.getCacheDir();
        File outputFile = File.createTempFile("image", "tmp", outputDir);
        return outputFile.getAbsolutePath();
    }

    private static void closeSilently(@Nullable Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (Throwable t) {
            // Do nothing
        }
    }

    public File getAlbumDir(Context context) {
        File storageDir = null;

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

            if (!storageDir.mkdirs() && !storageDir.exists()) {
                Log.d("CameraSample", "failed to create directory");
                return null;
            }

        } else {
            Log.v(context.getString(R.string.app_name), "External storage is not mounted " +
                    "READ/WRITE.");
        }

        return storageDir;
    }

    public File createImageFile() {
        // Create an placeholder file name
        Date date = new Date();
        String timeStamp = parseContent.dateFormat.format(date);
        timeStamp = timeStamp + "_" + parseContent.timeFormat.format(date);
        String imageFileName = "IMG_" + timeStamp + ".jpg";
        File albumF = getAlbumDir(context);
        return new File(albumF, imageFileName);
    }

    public String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {

                Utilities.handleException(Tag, e);

                result = "";
            }
            cursor.close();
        }
        return result;
    }

    public Uri checkExifRotation(Uri uri) {
        Bitmap photoBitmap;
        String imageFilePath = uri.getPath();
        int rotationAngle = 0;
        if (imageFilePath != null && imageFilePath.length() > 0) {

            try {
                ExifInterface exif = new ExifInterface(imageFilePath);
                String orientString = exif
                        .getAttribute(ExifInterface.TAG_ORIENTATION);
                int orientation = orientString != null ? Integer
                        .parseInt(orientString)
                        : ExifInterface.ORIENTATION_NORMAL;
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        rotationAngle = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        rotationAngle = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        rotationAngle = 270;
                        break;
                    case ExifInterface.ORIENTATION_NORMAL:
                        rotationAngle = 0;
                        break;
                    default:
                        // do with default
                        break;
                }
                Utilities.printLog(Tag, "Rotation : " + rotationAngle);
                if (rotationAngle != 0) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = false;
                    photoBitmap = BitmapFactory.decodeFile(imageFilePath, options);
                    if (photoBitmap != null) {
                        Matrix matrix = new Matrix();
                        matrix.setRotate(rotationAngle,
                                (float) photoBitmap.getWidth() / 2,
                                (float) photoBitmap.getHeight() / 2);
                        photoBitmap = Bitmap.createBitmap(photoBitmap, 0, 0,
                                photoBitmap.getWidth(),
                                photoBitmap.getHeight(), matrix, true);
                        String path = MediaStore.Images.Media.insertImage(
                                context.getContentResolver(), photoBitmap,
                                Calendar.getInstance().getTimeInMillis()
                                        + ".jpg", null);

                        return Uri.parse(path);
                    }
                } else {
                    return uri;
                }
            } catch (OutOfMemoryError e) {
                Utilities.printLog(ImageHelper.class.getName(), "out of Memory");
            } catch (IOException e) {
                Utilities.handleException(ImageHelper.class.getName(), e);
            }
        } else {
            Toast.makeText(
                    context,
                    "Error on path",
                    Toast.LENGTH_LONG).show();
        }
        return null;
    }

    public Uri compressAndResizeImage(Uri uri, int maxWidth, int maxHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap b = BitmapFactory.decodeFile(uri.getPath(), options);
        double imageHeight = options.outHeight;
        double imageWidth = options.outWidth;
        Log.i("IMG_HEIGHT", imageHeight + "");
        Log.i("IMG_WIDTH", imageWidth + "");
        Bitmap b2 = Bitmap.createScaledBitmap(b, maxWidth, maxHeight, false);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        b2.compress(Bitmap.CompressFormat.JPEG, 70, outStream);
        String path = MediaStore.Images.Media.insertImage(
                context.getContentResolver(), b2,
                Calendar.getInstance().getTimeInMillis()
                        + ".jpg", null);
        return Uri.parse(path);

    }
}
