package org.poo.card;

/**
 * Factory class that creates a card.
 */
public final class FactoryOfCard {
    /**
     * Private constructor to prevent instantiation.
     */
    private FactoryOfCard() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create a card based on the card type.
     *
     * @param cardType the type of card
     * @return the card
     */
    public static Card createCard(final String cardType) {
        return switch (cardType) {
            case "one-time" -> new OneTimeCard();
            case "normal" -> new NormalCard();
            default -> throw new IllegalArgumentException("Invalid card type");
        };
    }
}
