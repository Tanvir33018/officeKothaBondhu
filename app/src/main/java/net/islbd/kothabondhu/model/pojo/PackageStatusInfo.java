package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class PackageStatusInfo {
    @SerializedName("statusCode")
    private String statusCode;
    @SerializedName("description")
    private String description;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
