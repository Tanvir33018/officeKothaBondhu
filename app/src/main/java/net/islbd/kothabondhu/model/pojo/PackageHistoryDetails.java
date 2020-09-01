package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

public class PackageHistoryDetails {
    @SerializedName("buyMobileno")
    private String buyMobileno;
    @SerializedName("packBuydate")
    private String packBuydate;
    @SerializedName("packDuratonStatus")
    private String packDuratonStatus;
    @SerializedName("packDetails")
    private String packDetails;
    @SerializedName("packDuration")
    private String packDuration;
    @SerializedName("packExpireDate")
    private String packExpireDate;

    public String getBuyMobileno() {
        return buyMobileno;
    }

    public void setBuyMobileno(String buyMobileno) {
        this.buyMobileno = buyMobileno;
    }

    public String getPackBuydate() {
        return packBuydate;
    }

    public void setPackBuydate(String packBuydate) {
        this.packBuydate = packBuydate;
    }

    public String getPackDuratonStatus() {
        return packDuratonStatus;
    }

    public void setPackDuratonStatus(String packDuratonStatus) {
        this.packDuratonStatus = packDuratonStatus;
    }

    public String getPackDetails() {
        return packDetails;
    }

    public void setPackDetails(String packDetails) {
        this.packDetails = packDetails;
    }

    public String getPackDuration() {
        return packDuration;
    }

    public void setPackDuration(String packDuration) {
        this.packDuration = packDuration;
    }

    public String getPackExpireDate() {
        return packExpireDate;
    }

    public void setPackExpireDate(String packExpireDate) {
        this.packExpireDate = packExpireDate;
    }
}
