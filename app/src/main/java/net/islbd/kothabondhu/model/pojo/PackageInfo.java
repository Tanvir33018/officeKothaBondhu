package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

/**
 * Created by UserStatusDetails on 5/4/2019.
 */

public class PackageInfo {
    @SerializedName("pkg_id")
    private String pkgId;
    @SerializedName("package_details")
    private String packageDetails;
    @SerializedName("pkgid")
    private String pkgid;
    @SerializedName("pkg_duration")
    private String pkgDuration;
    @SerializedName("media")
    private String media;

    public String getPkgId() {
        return pkgId;
    }

    public void setPkgId(String pkgId) {
        this.pkgId = pkgId;
    }

    public String getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(String packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getPkgid() {
        return pkgid;
    }

    public void setPkgid(String pkgid) {
        this.pkgid = pkgid;
    }

    public String getPkgDuration() {
        return pkgDuration;
    }

    public void setPkgDuration(String pkgDuration) {
        this.pkgDuration = pkgDuration;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }
}
