package com.edelivery.store.models.datamodel;

/**
 * Created by Elluminati Mohit on 5/20/2017.
 */


public class InvoicePayment {

    private String title;
    private String value;
    private int imageId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
