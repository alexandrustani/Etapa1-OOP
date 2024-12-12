package org.poo.account;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.card.Card;
import org.poo.utils.Utils;

import java.util.ArrayList;

@Data
public class Account {
    private String accountIBAN;
    private ArrayList<Card> cards;
    private double minimumBalance;
    private String accountType;
    private double balance;
    private String currency;
    private ArrayList<ObjectNode> accountTransactions;
    private String alias;

    /**
     * Constructor for Account
     * @param currency for the account
     * @param accountType for the account
     */
    public Account(final String currency, final String accountType) {
        this.setAccountIBAN(Utils.generateIBAN());
        this.setBalance(Utils.INITIAL_BALANCE);
        this.setCurrency(currency);
        this.setAccountType(accountType);
        this.setCards(new ArrayList<>());
        this.setAccountTransactions(new ArrayList<>());
        this.setAlias(null);
    }

    /**
     * Add transaction to account
     * @param transaction to add
     */
    public void addTransaction(final ObjectNode transaction) {
        for (ObjectNode accountTransaction : accountTransactions) {
            if (accountTransaction.equals(transaction)) {
                return;
            }
        }

        accountTransactions.add(transaction);
    }

    /**
     * Add amount to balance
     * @param amountToAdd to add
     */
    public void addAmountToBalance(final double amountToAdd) {
        this.balance += amountToAdd;
    }

    /**
     * Subtract amount from balance
     * @param amountToSubtract to subtract
     */
    public void subtractAmountFromBalance(final double amountToSubtract) {
        this.balance -= amountToSubtract;
    }

    /**
     * Generate a new card number for the needed card
     * @param neededCard - the card for which the card number is to be generated
     */
    public void generateNewCardNumber(final Card neededCard) {
        for (Card card : cards) {
            if (neededCard.getCardNumber().equals(card.getCardNumber())) {
                card.setCardNumber(Utils.generateCardNumber());
            }
        }
    }
}
