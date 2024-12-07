package org.poo.card;

/**
 * Interface that represents a card.
 */
public interface Card {
    /**
     * Gets the card number.
     * @return the card number.
     */
    String getCardNumber();

    /**
     * Gets the card status.
     * @return the card status.
     */
    String getCardStatus();

    /**
     * Sets the card number.
     * @param cardNumber the card number.
     */
    void setCardNumber(String cardNumber);

    /**
     * Sets the card status.
     * @param cardStatus the card status.
     */
    void setCardStatus(String cardStatus);

    /**
     * Gets the card type.
     * @return the card type.
     */
    String getCardType();

    /**
     * Sets the card type.
     * @param cardType the card type.
     */
    void setCardType(String cardType);
}
