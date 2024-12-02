package org.poo.card;

import org.poo.utils.Utils;
public class Card {
    private String cardNumber;
    private String status;
    private String cardType;

    public Card(String status, String cardType) {
        this.setCardNumber(Utils.generateCardNumber());
        this.setStatus(status);
        this.setCardType(cardType);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
