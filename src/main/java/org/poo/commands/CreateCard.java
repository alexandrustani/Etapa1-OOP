package org.poo.commands;

import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;
import org.poo.card.FactoryOfCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.account.Account;

import java.util.ArrayList;

@Data
public class CreateCard {
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
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    neededUser = user;
                    break;
                }
            }
        }

        if (neededAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }

        neededAccount.getCards().add(FactoryOfCard.createCard(cardType));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        transaction.put("timestamp", command.getTimestamp());
        transaction.put("description", "New card created");

        neededUser.getTransactions().add(transaction);
    }
}
