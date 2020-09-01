package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class CommunicationDetails {
    @SerializedName("id")
    private Integer id;
    @SerializedName("type")
    private Integer type;
    @SerializedName("from_user_id")
    private Integer fromUserId;
    @SerializedName("to_user_id")
    private Integer toUserId;
    @SerializedName("conversation_room")
    private String conversationRoom;
    @SerializedName("status")
    private Integer status;
    @SerializedName("duration")
    private Integer duration;
    @SerializedName("time")
    private String time;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

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

    public String getConversationRoom() {
        return conversationRoom;
    }

    public void setConversationRoom(String conversationRoom) {
        this.conversationRoom = conversationRoom;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
