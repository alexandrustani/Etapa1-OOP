package org.poo.commands.cardRelatedCommands;

import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;
import org.poo.card.FactoryOfCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;

import java.util.ArrayList;

@Data
public final class CreateCard {
    /**
     * Utility class
     */
    private CreateCard() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create card type
     * @param command to execute
     * @return card type
     */
    public static String createCardType(final CommandInput command) {
        return switch (command.getCommand()) {
            case "createCard" -> "normal";
            case "createOneTimeCard" -> "one-time";
            default ->
                    throw new IllegalStateException("Unexpected Card command: "
                                                    + command.getCommand());
        };
    }

    /**
     * Execute the createCard command
     * @param command to execute
     * @param users to create card
     */
    public static void execute(final CommandInput command, final ArrayList<User> users) {
        Account neededAccount = null;
        User neededUser = null;
        String cardType = createCardType(command);

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())
                    && user.getEmail().equals(command.getEmail())) {
                    neededAccount = account;
                    neededUser = user;
                    break;
                }
            }
        }

        if (neededAccount == null) {
            return;
        }

        neededAccount.getCards().add(FactoryOfCard.createCard(cardType));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        transaction.put("account", neededAccount.getAccountIBAN());
        transaction.put("card", neededAccount.getCards().getLast().getCardNumber());
        transaction.put("cardHolder", neededUser.getEmail());
        transaction.put("description", "New card created");
        transaction.put("timestamp", command.getTimestamp());

        neededUser.addTransaction(transaction);
        neededAccount.addTransaction(transaction);
    }
}
