package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 11/9/2018.
 */

public class CommunicationStatus {
    @SerializedName("from_user_id")
    private Integer fromUserId;
    @SerializedName("to_user_id")
    private Integer toUserId;
    @SerializedName("old_status")
    private Integer oldStatus;
    @SerializedName("new_status")
    private Integer newStatus;

    public Integer getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(Integer fromUserId) {
        this.fromUserId = fromUserId;
    }

    public Integer getToUserId() {
        return toUserId;
    }

    public void setToUserId(Integer toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(Integer oldStatus) {
        this.oldStatus = oldStatus;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }
}
