package com.tvv.db.entity;

import java.util.Date;

public class Payment extends EntityID {
    private String guid;
    private User user;
    private String senderType;
    private Long senderId;
    private String recipientType;
    private Long recipientId;
    private Date timeOfLog;
    private String currency;
    private Double commission;
    private Double total;
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

    public String getSenderType() {
        return senderType;
    }

    public void setSenderType(String senderType) {
        this.senderType = senderType;
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

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public Date getTimeOfLog() {
        return timeOfLog;
    }

    public void setTimeOfLog(Date timeOfLog) {
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


    @Override
    public String toString() {
        return "Payment{" +
                "guid='" + guid + '\'' +
                ", user=" + user +
                ", senderType='" + senderType + '\'' +
                ", serderId=" + senderId +
                ", recipientType='" + recipientType + '\'' +
                ", recipientId=" + recipientId +
                ", timeOfLog=" + timeOfLog +
                ", commission=" + commission +
                ", total=" + total +
                ", status='" + status + '\'' +
                '}';
    }


}
