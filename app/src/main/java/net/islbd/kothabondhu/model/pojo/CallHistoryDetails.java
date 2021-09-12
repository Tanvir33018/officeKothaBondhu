package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by User on 5/5/2019.
 */

public class CallHistoryDetails {
    @SerializedName("agentId")
    private String agentId;
    @SerializedName("duration")
    private String duration;
    @SerializedName("callDate")
    private String callDate;
    @SerializedName("userId")
    private String userId;
    @SerializedName("status")
    private String status;

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
