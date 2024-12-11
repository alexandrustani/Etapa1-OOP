package org.poo.account;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import lombok.Setter;
import org.poo.card.Card;
import org.poo.user.CommerciantsDetails;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * ClassicAccount class
 */
@Getter
@Setter
public final class ClassicAccount implements Account {
    private String accountIBAN;
    private ArrayList<Card> cards;
    private double minimumBalance;
    private String accountType;
    private double balance;
    private String currency;
    private ArrayList<ObjectNode> accountTransactions;
    private ArrayList<CommerciantsDetails> commerciants;
    private String alias;

    /**
     * Constructor for ClassicAccount
     * @param currency for the account
     */
    public ClassicAccount(final String currency) {
        this.setAccountIBAN(Utils.generateIBAN());
        this.setBalance(Utils.INITIAL_BALANCE);
        this.setCurrency(currency);
        this.setAccountType(Utils.CLASSIC);
        this.setCards(new ArrayList<>());
        this.setTransactions(new ArrayList<>());
        this.setCommerciants(new ArrayList<>());
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

    @Override
    public ArrayList<ObjectNode> getTransactions() {
        return accountTransactions;
    }

    @Override
    public void addTransaction(final ObjectNode transaction) {
        for (ObjectNode accountTransaction : accountTransactions) {
            if (accountTransaction.get("timestamp").asInt()
                == transaction.get("timestamp").asInt()) {
                return;
            }
        }

        accountTransactions.add(transaction);
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

    /**
     * Add commerciant to user
     * @param commerciant to add
     */
    public void addCommerciant(final CommerciantsDetails commerciant) {
        for (CommerciantsDetails comm : this.getCommerciants()) {
            if (comm.getName().equals(commerciant.getName())) {
                comm.addAmount(commerciant.getAmount());
                return;
            }
        }

        this.getCommerciants().add(commerciant);
    }

    /**
     * Get a certain commerciant
     * @param commerciantName - the name of the commerciant
     * @return the commerciant
     */
    public CommerciantsDetails getACertainCommerciant(final String commerciantName) {
        for (CommerciantsDetails commerciant : this.getCommerciants()) {
            if (commerciant.getName().equals(commerciantName)) {
                return commerciant;
            }
        }

        return null;
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
