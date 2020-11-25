package net.islbd.kothabondhu.model.pojo;

import com.sinch.gson.annotations.SerializedName;

public class UserGmailInfo {
    @SerializedName("name")
    private  String name;
    @SerializedName("email")
    private String email;
    @SerializedName("id")
    private String id;

    public UserGmailInfo(String name, String email, String id) {
        this.name = name;
        this.email = email;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }



    @Override
    public String toString() {
        return "UserGmailInfo{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
