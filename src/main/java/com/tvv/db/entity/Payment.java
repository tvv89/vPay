package com.tvv.db.entity;


public class Payment extends EntityID {
    private String guid;
    private User user;
    private Long senderId;
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
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
                ", senderId=" + senderId +
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
