package org.poo.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.card.Card;

import java.util.ArrayList;
import org.poo.utils.Utils;

/**
 * SavingsAccount class
 */
@Setter
@Getter
public final class SavingsAccount implements Account {
    private String accountIBAN;
    private ArrayList<Card> cards;
    private double minimumBalance;
    private String accountType;
    private double balance;
    private String currency;
    private double interestRate;
    private ArrayList<ObjectNode> accountTransactions;
    private String alias;

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

    /**
     * Constructor for SavingsAccount
     * @param currency for the account
     * @param interestRate for the account
     */
    public SavingsAccount(final String currency, final double interestRate) {
        this.setAccountIBAN(Utils.generateIBAN());
        this.setBalance(Utils.INITIAL_BALANCE);
        this.setCurrency(currency);
        this.setAccountType(Utils.SAVINGS);
        this.setInterestRate(interestRate);
        this.setCards(new ArrayList<>());
        this.setAccountTransactions(new ArrayList<>());
        this.setAlias(null);
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

    @Override
    public ArrayList<ObjectNode> getTransactions() {
        return accountTransactions;
    }

    @Override
    public void addTransaction(final ObjectNode transaction) {
        for (ObjectNode accountTransaction : this.getTransactions()) {
            if (accountTransaction.get("timestamp").asInt()
                == transaction.get("timestamp").asInt()) {
                return;
            }
        }

        this.getTransactions().add(transaction);
    }

    @Override
    public void setTransactions(final ArrayList<ObjectNode> transactions) {
        this.accountTransactions = transactions;
    }

    @Override
    public void addAmountToBalance(final double amountToAdd) {
        this.balance += amountToAdd;
    }

    @Override
    public void subtractAmountFromBalance(final double amountToSubtract) {
        this.balance -= amountToSubtract;
    }

    @Override
    public String getAlias() {
        return this.alias;
    }

    @Override
    public void setAlias(final String newAlias) {
        this.alias = newAlias;
    }
}
