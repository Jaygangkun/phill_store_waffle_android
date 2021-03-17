package com.edelivery.store;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.edelivery.store.parse.ParseContent;
import com.edelivery.store.persistentroomdata.NotificationRepository;
import com.edelivery.store.utils.Constant;
import com.edelivery.store.utils.PreferenceHelper;
import com.edelivery.store.utils.Utilities;
import com.elluminati.edelivery.store.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

public class FcmMessagingService extends FirebaseMessagingService {

    public static final String MESSAGE = "message";
    public static final String PUSH_DATA1 = "push_data1";
    public static final String NEW_ORDER = "2071";
    public static final String DELIVERY_MAN_ACCEPTED = "2061";
    public static final String DELIVERY_MAN_COMING = "2062";
    public static final String DELIVERY_MAN_ARRIVED = "2063";
    public static final String DELIVERY_MAN_PICKED_ORDER = "2064";
    public static final String DELIVERY_MAN_STARTED_DELIVERY = "2065";
    public static final String DELIVERY_MAN_ARRIVED_AT_DESTINATION = "2066";
    public static final String DELIVERY_MAN_COMPLETE_DELIVERY = "2067";
    public static final String USER_CANCELLED_ORDER = "2068";
    public static final String DELIVERY_MAN_NOT_FOUND = "2069";
    public static final String NEW_SCHEDULE_ORDER = "2070";
    public static final String STORE_APPROVED = "2072";
    public static final String STORE_DECLINED = "2073";
    public static final String USER_ACCEPT_EDIT_ORDER = "2074";
    public static final String LOGIN_IN_OTHER_DEVICE = "2093";
    public static final String USER_UPDATE_ORDER_DETAIL = "2088";
    private static final String CHANNEL_ID = "channel_01";
    private Map<String, String> data;

    public FcmMessagingService() {

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {


        if (remoteMessage != null) {
            Utilities.printLog("FcmMessagingService", "From:" + remoteMessage.getFrom());
            Utilities.printLog("FcmMessagingService", "Data:" + remoteMessage.getData());
            data = remoteMessage.getData();
            String message = data.get(MESSAGE);
            if (!TextUtils.isEmpty(message)) {
                sendNotification(message);
            }
            else if(remoteMessage.getNotification() != null && remoteMessage.getNotification().getBody() != null)
            {
                sendNotification(remoteMessage.getNotification().getBody());
            }
        }
    }


    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void registerComponentCallbacks(ComponentCallbacks callback) {
        super.registerComponentCallbacks(callback);
    }

    @Override
    public void onSendError(String s, Exception e) {
        super.onSendError(s, e);
        Log.i("FcmMessagingService", e.getLocalizedMessage());
        e.printStackTrace();
        Log.i("FcmMessagingService", s);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void generateNotification(String message, int activityId) {

        int notificationId = 2017;
        Intent notificationIntent = null;

        switch (activityId) {
            case Constant.LOGIN_ACTIVITY:
                notificationIntent = new Intent(this, RegisterLoginActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                        .FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
            case Constant.HOME_ACTIVITY:
                notificationIntent = getPackageManager().getLaunchIntentForPackage
                        (getPackageName());
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                break;
            default:
                // do with default
                break;
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(mChannel);
        }


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(RegisterLoginActivity.class);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        final Notification.Builder notificationBuilder = new Notification.Builder
                (this).setPriority(Notification.PRIORITY_MAX).setContentTitle(this.getResources().getString(R.string
                .app_name)).setContentText(message).setAutoCancel(true).setSmallIcon(getNotificationIcon()).setContentIntent(notificationPendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }
        if (PreferenceHelper.getPreferenceHelper(this).getIsPushNotificationSoundOn()) {
            notificationBuilder.setDefaults(Notification.DEFAULT_SOUND
                    | Notification.DEFAULT_LIGHTS);
        }


        notificationManager.notify(notificationId, notificationBuilder.build());
    }


    private void sendNotification(String message) {
        switch (message) {
            case NEW_ORDER:
                generateNotification(getMessage(message), Constant.HOME_ACTIVITY);
                if (!TextUtils.isEmpty(data.get(PUSH_DATA1))) {
                    sendBroadcastWithData(Constant.Action.ACTION_NEW_ORDER_ACTION, data.get
                            (PUSH_DATA1));
                }
                saveNotification(getMessage(message),
                        com.edelivery.store.persistentroomdata.Notification.ORDER_TYPE);
                break;
            case DELIVERY_MAN_ACCEPTED:
            case DELIVERY_MAN_COMING:
            case DELIVERY_MAN_ARRIVED:
            case DELIVERY_MAN_PICKED_ORDER:
            case DELIVERY_MAN_STARTED_DELIVERY:
            case DELIVERY_MAN_ARRIVED_AT_DESTINATION:
            case DELIVERY_MAN_COMPLETE_DELIVERY:
            case DELIVERY_MAN_NOT_FOUND:
            case USER_CANCELLED_ORDER:
            case USER_ACCEPT_EDIT_ORDER:
            case NEW_SCHEDULE_ORDER:
            case USER_UPDATE_ORDER_DETAIL:
                generateNotification(getMessage(message), Constant.HOME_ACTIVITY);
                sendBroadcast(Constant.Action.ACTION_ORDER_STATUS_ACTION);
                saveNotification(getMessage(message),
                        com.edelivery.store.persistentroomdata.Notification.ORDER_TYPE);
                break;
            case STORE_APPROVED:
                generateNotification(getMessage(message), Constant.HOME_ACTIVITY);
                sendBroadcast(Constant.Action.ACTION_STORE_APPROVED);
                saveNotification(getMessage(message),
                        com.edelivery.store.persistentroomdata.Notification.ORDER_TYPE);
                break;
            case STORE_DECLINED:
                generateNotification(getMessage(message), Constant.HOME_ACTIVITY);
                sendBroadcast(Constant.Action.ACTION_STORE_DECLINED);
                saveNotification(getMessage(message),
                        com.edelivery.store.persistentroomdata.Notification.ORDER_TYPE);
                break;
            case LOGIN_IN_OTHER_DEVICE:
                generateNotification(getMessage(message), Constant.LOGIN_ACTIVITY);
                break;
            default:
                generateNotification(getMessage(message), Constant.HOME_ACTIVITY);
                saveNotification(getMessage(message),
                        com.edelivery.store.persistentroomdata.Notification.MASS_TYPE);
                break;

        }
    }

    private String getMessage(String code) {

        String msg = "";
        try {
            String messageCode = Constant.PUSH_NOTIFICATION_PREFIX + code;
            msg = this.getResources().getString(
                    this.getResources().getIdentifier(messageCode, Constant.STRING,
                            this.getPackageName()));
        } catch (Exception e) {
            Utilities.handleException("FcmMessagingService", e);
            msg = code;
        }

        return msg;
    }

    private void sendBroadcast(String action) {
        sendBroadcast(new Intent(action));
    }

    private void sendBroadcastWithData(String action, String pushData) {
        Intent intent = new Intent(action);
        if (!TextUtils.isEmpty(pushData)) {
            intent.putExtra(Constant.PUSH_DATA, pushData);
        }

        sendBroadcast(intent);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build
                .VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable
                .ic_stat_store : R.mipmap.ic_launcher;
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Utilities.printLog(FcmMessagingService.class.getSimpleName(), "Refreshed token: " + token);
        PreferenceHelper.getPreferenceHelper(getApplicationContext()).putDeviceToken(token);
        Intent intent = new Intent(Constant.DEVICE_TOKEN);
        intent.putExtra(Constant.DEVICE_TOKEN_RECEIVED, true);
        getApplicationContext().sendBroadcast(intent);
    }

    private void saveNotification(String message, int notificationType) {

        com.edelivery.store.persistentroomdata.Notification notification =
                new com.edelivery.store.persistentroomdata.Notification();
        notification.setMessage(message);
        notification.setNotificationType(notificationType);
        notification.setDate(ParseContent.getParseContentInstance().dateTimeFormat_am.format(new Date()));
        NotificationRepository.getInstance(this).insertNotification(notification,
                null);
    }
}
