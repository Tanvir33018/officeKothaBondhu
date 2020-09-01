package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 7/1/2019.
 */

public class UserInfo {
    @SerializedName("endUserRegId")
    private String endUserRegId;
    @SerializedName("endUserId")
    private String endUserId;
    @SerializedName("subStatus")
    private String subStatus;
    @SerializedName("name")
    private String name;
    @SerializedName("activeDate")
    private String activeDate;
    @SerializedName("userAge")
    private String userAge;
    @SerializedName("userLocation")
    private String userLocation;
    @SerializedName("usergender")
    private String usergender;

    public String getEndUserRegId() {
        return endUserRegId;
    }

    public void setEndUserRegId(String endUserRegId) {
        this.endUserRegId = endUserRegId;
    }

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }

    public String getSubStatus() {
        return subStatus;
    }

    public void setSubStatus(String subStatus) {
        this.subStatus = subStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveDate() {
        return activeDate;
    }

    public void setActiveDate(String activeDate) {
        this.activeDate = activeDate;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

}
