package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class CallHistoryDetailsSecond {
    @SerializedName("callDuration")
    private String callDuration;
    @SerializedName("callDate")
    private String callDate;
    @SerializedName("agentName")
    private String agentName;

    public String getCallDuration() {
        return callDuration;
    }

    public String getCallDate() {
        return callDate;
    }

    public String getAgentName() {
        return agentName;
    }
}
