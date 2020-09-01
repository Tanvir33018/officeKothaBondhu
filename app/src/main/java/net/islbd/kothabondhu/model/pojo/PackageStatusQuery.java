package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 5/5/2019.
 */

public class PackageStatusQuery {
    @SerializedName("endUserRegId")
    private String endUserRegId;

    public String getEndUserRegId() {
        return endUserRegId;
    }

    public void setEndUserRegId(String endUserRegId) {
        this.endUserRegId = endUserRegId;
    }
}
