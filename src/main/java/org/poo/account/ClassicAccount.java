package org.poo.account;
import org.poo.card.Card;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * ClassicAccount class
 */
public final class ClassicAccount implements Account {
    private String accountIBAN;
    private ArrayList<Card> cards;
    private static final double INITIAL_BALANCE = 0;
    private double minimumBalance;
    private static final String CLASSIC = "classic";
    private String accountType;
    private double balance;
    private String currency;

    /**
     * Constructor for ClassicAccount
     * @param currency for the account
     */
    public ClassicAccount(final String currency) {
        this.setAccountIBAN(Utils.generateIBAN());
        this.setBalance(INITIAL_BALANCE);
        this.setCurrency(currency);
        this.setAccountType(CLASSIC);
        this.setCards(new ArrayList<>());
    }

    @Override
    public double getMinimumBalance() {
        return this.minimumBalance;
    }

    @Override
    public void setMinimumBalance(final double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    @Override
    public String getAccountType() {
        return this.accountType;
    }

    @Override
    public void setAccountType(final String accountType) {
        this.accountType = accountType;
    }

    @Override
    public double getBalance() {
        return this.balance;
    }

    @Override
    public void setBalance(final double balance) {
        this.balance = balance;
    }

    @Override
    public String getCurrency() {
        return this.currency;
    }

    @Override
    public void setCurrency(final String currency) {
        this.currency = currency;
    }

    @Override
    public String getAccountIBAN() {
        return this.accountIBAN;
    }

    @Override
    public void setAccountIBAN(final String newIBAN) {
        this.accountIBAN = newIBAN;
    }

    @Override
    public ArrayList<Card> getCards() {
        return cards;
    }

    @Override
    public void setCards(final ArrayList<Card> newCards) {
        this.cards = newCards;
    }
}
