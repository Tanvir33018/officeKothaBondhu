package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 5/26/2019.
 */

public class UserQuery {
    @SerializedName("endUserId")
    private String endUserId;

    public String getEndUserId() {
        return endUserId;
    }

    public void setEndUserId(String endUserId) {
        this.endUserId = endUserId;
    }
}
