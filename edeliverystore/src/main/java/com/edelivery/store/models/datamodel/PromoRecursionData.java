package com.edelivery.store.models.datamodel;

/**
 * Created by elluminati on 19-Jan-18.
 */

public class PromoRecursionData {
    private String displayData;
    private String requestData;
    private boolean selected;

    public PromoRecursionData(String displayData, String requestData, boolean selected) {
        this.displayData = displayData;
        this.requestData = requestData;
        this.selected = selected;
    }

    public String getDisplayData() {

        return displayData;
    }

    public void setDisplayData(String displayData) {
        this.displayData = displayData;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
