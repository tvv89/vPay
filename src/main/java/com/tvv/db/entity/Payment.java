package com.tvv.db.entity;

/**
 * Class for payment object
 * guid - id of payment, generate automatically in system
 * user - object User, user owner
 * sender - account which create payment
 * recipientType - type of recipient (Account; Card)
 * recipientId - recipient id (for Account: IBAN, for Card: card number)
 * timeOfLog - time of payment
 * currency - account currency which create payment
 * commission - commission of payment
 * total - total sum with commission
 * currencySum - currency of payment
 * sum - sum of payment
 * status  - payment status (Ready, Submitted)
 */

public class Payment extends EntityID {
    private String guid;
    private User user;
    private Account sender;
    private String recipientType;
    private String recipientId;
    private String timeOfLog;
    private String currency;
    private Double commission;
    private Double total;
    private String currencySum;
    private Double sum;
    private String status;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Account getSenderId() {
        return sender;
    }

    public void setSenderId(Account sender) {
        this.sender = sender;
    }

    public String getRecipientType() {
        return recipientType;
    }

    public void setRecipientType(String recipientType) {
        this.recipientType = recipientType;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getTimeOfLog() {
        return timeOfLog;
    }

    public void setTimeOfLog(String timeOfLog) {
        this.timeOfLog = timeOfLog;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySum() {
        return currencySum;
    }

    public void setCurrencySum(String currencySum) {
        this.currencySum = currencySum;
    }

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "guid='" + guid + '\'' +
                ", user=" + user +
                ", senderId=" + sender +
                ", recipientType='" + recipientType + '\'' +
                ", recipientId='" + recipientId + '\'' +
                ", timeOfLog='" + timeOfLog + '\'' +
                ", currency='" + currency + '\'' +
                ", commission=" + commission +
                ", total=" + total +
                ", currencySum='" + currencySum + '\'' +
                ", sum=" + sum +
                ", status='" + status + '\'' +
                '}';
    }
}
