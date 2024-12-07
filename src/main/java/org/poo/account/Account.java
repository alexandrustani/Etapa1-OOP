package org.poo.account;

import org.poo.card.Card;

import java.util.ArrayList;

/**
 * Interface that represents an account.
 */
public interface Account {
    /**
     * Gets the minimum balance.
     * @return the minimum balance.
     */
    double getMinimumBalance();

    /**
     * Sets the minimum balance.
     * @param minimumBalance the minimum balance.
     */
    void setMinimumBalance(double minimumBalance);

    /**
     * Gets the account type.
     * @return the account type.
     */
    String getAccountType();

    /**
     * Sets the account type.
     * @param accountType the account type.
     */
    void setAccountType(String accountType);

    /**
     * Gets the balance.
     * @return the balance.
     */
    double getBalance();

    /**
     * Sets the balance.
     * @param balance the balance.
     */
    void setBalance(double balance);

    /**
     * Gets the currency.
     * @return the currency.
     */
    String getCurrency();

    /**
     * Sets the currency.
     * @param currency the currency.
     */
    void setCurrency(String currency);

    /**
     * Gets the IBAN.
     * @return the IBAN.
     */
    String getAccountIBAN();

    /**
     * Sets the IBAN.
     * @param newIBAN the new IBAN.
     */
    void setAccountIBAN(String newIBAN);

    /**
     * Gets the cards.
     * @return the cards.
     */
    ArrayList<Card> getCards();

    /**
     * Sets the cards.
     * @param newCards the cards.
     */
    void setCards(ArrayList<Card> newCards);
}
