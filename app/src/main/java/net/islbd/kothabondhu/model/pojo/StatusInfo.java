package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 5/4/2019.
 */

public class StatusInfo {
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("descrption")
    private String descrption;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }
}
