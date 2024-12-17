package org.poo.card;

import lombok.Data;
import org.poo.utils.Utils;

/**
 * Class representing a card.
 */
@Data
public final class Card {
    private String cardNumber;
    private String cardStatus;
    private String cardType;

    public Card(final String cardType) {
        this.setCardNumber(Utils.generateCardNumber());
        this.setCardType(cardType);
        this.setCardStatus(Utils.ACTIVE);
    }
}
