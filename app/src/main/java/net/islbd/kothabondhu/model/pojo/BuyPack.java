package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class BuyPack {
    @SerializedName("endUserRegId")
    private String endUserRegId;
    @SerializedName("packId")
    private String packId;
    @SerializedName("packBuyMedia")
    private String packBuyMedia;
    @SerializedName("packBuyMobileNo")
    private String packBuyMobileNo;

    public String getEndUserRegId() {
        return endUserRegId;
    }

    public void setEndUserRegId(String endUserRegId) {
        this.endUserRegId = endUserRegId;
    }

    public String getPackId() {
        return packId;
    }

    public void setPackId(String packId) {
        this.packId = packId;
    }

    public String getPackBuyMedia() {
        return packBuyMedia;
    }

    public void setPackBuyMedia(String packBuyMedia) {
        this.packBuyMedia = packBuyMedia;
    }

    public String getPackBuyMobileNo() {
        return packBuyMobileNo;
    }

    public void setPackBuyMobileNo(String packBuyMobileNo) {
        this.packBuyMobileNo = packBuyMobileNo;
    }
}
