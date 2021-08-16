package net.islbd.kothabondhu.model.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class NagadResponse {

    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("orderdatetime")
    @Expose
    private String orderdatetime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("statusCode")
    @Expose
    private String statusCode;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOrderdatetime() {
        return orderdatetime;
    }

    public void setOrderdatetime(String orderdatetime) {
        this.orderdatetime = orderdatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

}
