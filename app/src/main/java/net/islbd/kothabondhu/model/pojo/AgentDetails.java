package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 5/5/2019.
 */

public class AgentDetails {
    @SerializedName("agentId")
    private String agentId;
    @SerializedName("agentName")
    private String agentName;
    @SerializedName("agentProfilePic")
    private String agentProfilePic;
    @SerializedName("OnlineStatus")
    private String onlineStatus;
    @SerializedName("agentAge")
    private String agentAge;

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAgentProfilePic() {
        return agentProfilePic;
    }

    public void setAgentProfilePic(String agentProfilePic) {
        this.agentProfilePic = agentProfilePic;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAgentAge() {
        return agentAge;
    }

    public void setAgentAge(String agentAge) {
        this.agentAge = agentAge;
    }
}
