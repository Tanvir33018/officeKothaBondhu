package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 5/5/2019.
 */

public class UserStatusDetails {
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
}
