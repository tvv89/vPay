package com.tvv.db.entity;

/**
 * Class for Account. Use data from table accounts
 * iban - account id, use for create payment
 * ipn - reserved
 * bankCode - reserved
 * name - name of account
 * currency - currency of account
 * balance - balance account (double format #.##)
 * ownerUser - object user, who own account
 * status - Enabled, Idle, Disabled
 * card - object card, which top up balance account
 */
public class Account extends EntityID {
    private String iban;
    private String ipn;
    private String bankCode;
    private String name;
    private String currency;
    private Double balance;
    private User ownerUser;
    private String status;
    private Card card;

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public String getIpn() {
        return ipn;
    }

    public void setIpn(String ipn) {
        this.ipn = ipn;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public User getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Accounts{" +
                "id='" + getId() + '\'' +
                "iban='" + iban + '\'' +
                ", ipn='" + ipn + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", name='" + name + '\'' +
                ", currency='" + currency + '\'' +
                ", balance=" + balance +
                ", ownerUser=" + ownerUser.getLogin() +
                ", status='" + status + '\'' +
                '}';
    }
}
