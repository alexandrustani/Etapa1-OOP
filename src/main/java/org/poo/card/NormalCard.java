package org.poo.card;

import lombok.Getter;
import lombok.Setter;
import org.poo.utils.Utils;

@Getter
@Setter
public final class NormalCard implements Card {
    private String cardNumber;
    private String cardStatus;
    private String cardType;

    public NormalCard() {
        this.setCardNumber(Utils.generateCardNumber());
        this.setCardStatus(Utils.ACTIVE);
        this.setCardType(Utils.CARD_TYPE);
    }

    @Override
    public String getCardNumber() {
        return this.cardNumber;
    }

    @Override
    public String getCardStatus() {
        return this.cardStatus;
    }

    @Override
    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void setCardStatus(final String cardStatus) {
        this.cardStatus = cardStatus;
    }

    @Override
    public String getCardType() {
        return this.cardType;
    }

    @Override
    public void setCardType(final String cardType) {
        this.cardType = cardType;
    }
}
