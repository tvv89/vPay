package com.tvv.db.entity;

import java.util.Date;

public class Card extends EntityID {
    private String name;
    private String number;
    private String expDate;
    private Account account;
    private Boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id='" + getId() + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", expDate='" + expDate + '\'' +
                ", account=" + account +
                ", status=" + status +
                '}';
    }
}
