package com.edelivery.store.models.datamodel;

/**
 * Created by Elluminati Mohit on 5/11/2017.
 */

public class StoreOpenTime {

    String openTimeHour;
    String openTimeMinute;
    String closeTimeHour;
    String closeTimeMinute;

    public String getOpenTimeHour() {
        return openTimeHour;
    }

    public void setOpenTimeHour(String openTimeHour) {
        this.openTimeHour = openTimeHour;
    }

    public String getOpenTimeMinute() {
        return openTimeMinute;
    }

    public void setOpenTimeMinute(String openTimeMinute) {
        this.openTimeMinute = openTimeMinute;
    }

    public String getCloseTimeHour() {
        return closeTimeHour;
    }

    public void setCloseTimeHour(String closeTimeHour) {
        this.closeTimeHour = closeTimeHour;
    }

    public String getCloseTimeMinute() {
        return closeTimeMinute;
    }

    public void setCloseTimeMinute(String closeTimeMinute) {
        this.closeTimeMinute = closeTimeMinute;
    }
}
