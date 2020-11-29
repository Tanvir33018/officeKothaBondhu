package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by User on 7/1/2019.
 */

public class UserAccountInfo {
    @SerializedName("UserInfo")
    private UserInfo userInfo;
    @SerializedName("callHistory")
    private List<CallHistoryDetailsSecond> callHistory = null;
    @SerializedName("packageDetails")
    private List<PackageHistoryDetails> packageHistoryDetailsList = null;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<CallHistoryDetailsSecond> getCallHistory() {
        return callHistory;
    }

    public void setCallHistory(List<CallHistoryDetailsSecond> callHistory) {
        this.callHistory = callHistory;
    }

    public List<PackageHistoryDetails> getPackageHistoryDetailsList() {
        return packageHistoryDetailsList;
    }

    public void setPackageHistoryDetailsList(List<PackageHistoryDetails> packageHistoryDetailsList) {
        this.packageHistoryDetailsList = packageHistoryDetailsList;
    }
}
