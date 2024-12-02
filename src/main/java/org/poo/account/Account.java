package org.poo.account;

import org.poo.card.Card;
import org.poo.utils.Utils;

import java.util.ArrayList;

public class Account {
    private static final double DEFAULT_BALANCE = 0.0;
    private String accountType;
    private double balance;
    private double interestRate;
    private String currency;
    private String IBAN;
    private ArrayList<Card> cards;

    public Account(final String accountType, final double interestRate, final String currency) {
        this.setAccountType(accountType);
        this.setBalance(DEFAULT_BALANCE);
        this.setInterestRate(interestRate);
        this.setCurrency(currency);
        this.setIBAN(Utils.generateIBAN());
        this.cards = new ArrayList<>();
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
}
