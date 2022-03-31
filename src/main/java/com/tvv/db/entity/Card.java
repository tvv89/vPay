package com.tvv.db.entity;


public class Card extends EntityID {
    private String name;
    private String number;
    private String expDate;
    private User user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
                ", account=" + user +
                ", status=" + status +
                '}';
    }
}
