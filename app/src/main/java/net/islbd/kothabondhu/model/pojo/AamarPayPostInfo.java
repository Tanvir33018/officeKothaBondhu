package net.islbd.kothabondhu.model.pojo;



import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AamarPayPostInfo {

    @SerializedName("pg_txnid")
    @Expose
    private String pgTxnid;
    @SerializedName("mer_txnid")
    @Expose
    private String merTxnid;
    @SerializedName("risk_title")
    @Expose
    private String riskTitle;
    @SerializedName("risk_level")
    @Expose
    private Object riskLevel;
    @SerializedName("cus_name")
    @Expose
    private String cusName;
    @SerializedName("cus_email")
    @Expose
    private String cusEmail;
    @SerializedName("cus_phone")
    @Expose
    private String cusPhone;
    @SerializedName("desc")
    @Expose
    private String desc;
    @SerializedName("merchant_id")
    @Expose
    private String merchantId;
    @SerializedName("store_id")
    @Expose
    private String storeId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("amount_bdt")
    @Expose
    private String amountBdt;
    @SerializedName("pay_status")
    @Expose
    private String payStatus;
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("status_title")
    @Expose
    private String statusTitle;
    @SerializedName("cardnumber")
    @Expose
    private String cardnumber;
    @SerializedName("approval_code")
    @Expose
    private String approvalCode;
    @SerializedName("payment_processor")
    @Expose
    private String paymentProcessor;
    @SerializedName("bank_trxid")
    @Expose
    private String bankTrxid;
    @SerializedName("payment_type")
    @Expose
    private String paymentType;
    @SerializedName("error_code")
    @Expose
    private String errorCode;
    @SerializedName("error_title")
    @Expose
    private String errorTitle;
    @SerializedName("bin_country")
    @Expose
    private String binCountry;
    @SerializedName("bin_issuer")
    @Expose
    private String binIssuer;
    @SerializedName("bin_cardtype")
    @Expose
    private String binCardtype;
    @SerializedName("bin_cardcategory")
    @Expose
    private String binCardcategory;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("date_processed")
    @Expose
    private String dateProcessed;
    @SerializedName("amount_currency")
    @Expose
    private String amountCurrency;
    @SerializedName("rec_amount")
    @Expose
    private String recAmount;
    @SerializedName("processing_ratio")
    @Expose
    private String processingRatio;
    @SerializedName("processing_charge")
    @Expose
    private String processingCharge;
    @SerializedName("ip")
    @Expose
    private String ip;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currency_merchant")
    @Expose
    private String currencyMerchant;
    @SerializedName("convertion_rate")
    @Expose
    private String convertionRate;
    @SerializedName("opt_a")
    @Expose
    private String optA;
    @SerializedName("opt_b")
    @Expose
    private String optB;
    @SerializedName("opt_c")
    @Expose
    private String optC;
    @SerializedName("opt_d")
    @Expose
    private String optD;
    @SerializedName("verify_status")
    @Expose
    private String verifyStatus;
    @SerializedName("call_type")
    @Expose
    private String callType;
    @SerializedName("email_send")
    @Expose
    private String emailSend;
    @SerializedName("doc_recived")
    @Expose
    private String docRecived;
    @SerializedName("checkout_status")
    @Expose
    private String checkoutStatus;

    public String getPgTxnid() {
        return pgTxnid;
    }

    public void setPgTxnid(String pgTxnid) {
        this.pgTxnid = pgTxnid;
    }

    public String getMerTxnid() {
        return merTxnid;
    }

    public void setMerTxnid(String merTxnid) {
        this.merTxnid = merTxnid;
    }

    public String getRiskTitle() {
        return riskTitle;
    }

    public void setRiskTitle(String riskTitle) {
        this.riskTitle = riskTitle;
    }

    public Object getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(Object riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getCusEmail() {
        return cusEmail;
    }

    public void setCusEmail(String cusEmail) {
        this.cusEmail = cusEmail;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getAmountBdt() {
        return amountBdt;
    }

    public void setAmountBdt(String amountBdt) {
        this.amountBdt = amountBdt;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusTitle() {
        return statusTitle;
    }

    public void setStatusTitle(String statusTitle) {
        this.statusTitle = statusTitle;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public void setApprovalCode(String approvalCode) {
        this.approvalCode = approvalCode;
    }

    public String getPaymentProcessor() {
        return paymentProcessor;
    }

    public void setPaymentProcessor(String paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }

    public String getBankTrxid() {
        return bankTrxid;
    }

    public void setBankTrxid(String bankTrxid) {
        this.bankTrxid = bankTrxid;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public void setErrorTitle(String errorTitle) {
        this.errorTitle = errorTitle;
    }

    public String getBinCountry() {
        return binCountry;
    }

    public void setBinCountry(String binCountry) {
        this.binCountry = binCountry;
    }

    public String getBinIssuer() {
        return binIssuer;
    }

    public void setBinIssuer(String binIssuer) {
        this.binIssuer = binIssuer;
    }

    public String getBinCardtype() {
        return binCardtype;
    }

    public void setBinCardtype(String binCardtype) {
        this.binCardtype = binCardtype;
    }

    public String getBinCardcategory() {
        return binCardcategory;
    }

    public void setBinCardcategory(String binCardcategory) {
        this.binCardcategory = binCardcategory;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateProcessed() {
        return dateProcessed;
    }

    public void setDateProcessed(String dateProcessed) {
        this.dateProcessed = dateProcessed;
    }

    public String getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(String amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public String getRecAmount() {
        return recAmount;
    }

    public void setRecAmount(String recAmount) {
        this.recAmount = recAmount;
    }

    public String getProcessingRatio() {
        return processingRatio;
    }

    public void setProcessingRatio(String processingRatio) {
        this.processingRatio = processingRatio;
    }

    public String getProcessingCharge() {
        return processingCharge;
    }

    public void setProcessingCharge(String processingCharge) {
        this.processingCharge = processingCharge;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencyMerchant() {
        return currencyMerchant;
    }

    public void setCurrencyMerchant(String currencyMerchant) {
        this.currencyMerchant = currencyMerchant;
    }

    public String getConvertionRate() {
        return convertionRate;
    }

    public void setConvertionRate(String convertionRate) {
        this.convertionRate = convertionRate;
    }

    public String getOptA() {
        return optA;
    }

    public void setOptA(String optA) {
        this.optA = optA;
    }

    public String getOptB() {
        return optB;
    }

    public void setOptB(String optB) {
        this.optB = optB;
    }

    public String getOptC() {
        return optC;
    }

    public void setOptC(String optC) {
        this.optC = optC;
    }

    public String getOptD() {
        return optD;
    }

    public void setOptD(String optD) {
        this.optD = optD;
    }

    public String getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(String verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getEmailSend() {
        return emailSend;
    }

    public void setEmailSend(String emailSend) {
        this.emailSend = emailSend;
    }

    public String getDocRecived() {
        return docRecived;
    }

    public void setDocRecived(String docRecived) {
        this.docRecived = docRecived;
    }

    public String getCheckoutStatus() {
        return checkoutStatus;
    }

    public void setCheckoutStatus(String checkoutStatus) {
        this.checkoutStatus = checkoutStatus;
    }

}
