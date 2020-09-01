package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class Agent {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("pic")
    private String fullcontentUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullcontentUrl() {
        return fullcontentUrl;
    }

    public void setFullcontentUrl(String fullcontentUrl) {
        this.fullcontentUrl = fullcontentUrl;
    }
}
