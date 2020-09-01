package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 5/4/2019.
 */

public class PackageInfoQuery {
    @SerializedName("q")
    private String q;
    @SerializedName("mobilenumber")
    private String mobilenumber;

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }
}
