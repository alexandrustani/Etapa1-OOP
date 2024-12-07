package org.poo.commands;

import lombok.Data;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.user.User;

import java.util.ArrayList;

/**
 * Command to delete a card.
 */
@Data
public final class DeleteCard {
    /**
     * Utility class.
     */
    private DeleteCard() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Executes the command.
     */
    public static void execute(CommandInput command, ArrayList<User> users) {
        Account neededAccount = null;
        Card neededCard = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        neededCard = card;
                        neededAccount = account;
                        break;
                    }
                }
            }
        }

        if (neededAccount == null) {
            return;
        }

        neededAccount.getCards().remove(neededCard);
    }
}
