package org.poo.commands.cardRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.utils.Utils;

import java.util.ArrayList;

/**
 * Command to check the status of a card.
 */
@Data
public final class CheckCardStatus {
    /**
     * Utility class.
     */
    private CheckCardStatus() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Execute the checkCardStatus command.
     * @param command - the command to be executed
     * @param users - the list of users
     * @param output - the output array
     */
    public static void execute(final CommandInput command,
                               final ArrayList<User> users,
                               final ArrayNode output) {
        Account neededAccount = null;
        Card neededCard = null;

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandNode = mapper.createObjectNode();
        commandNode.put("command", "checkCardStatus");

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
            ObjectNode error = mapper.createObjectNode();
            error.put("description", "Card not found");
            error.put("timestamp", command.getTimestamp());
            commandNode.set("output", error);
            commandNode.put("timestamp", command.getTimestamp());
            output.add(commandNode);

            return;
        }

        if (neededAccount.getMinimumBalance() >= neededAccount.getBalance()) {
            neededCard.setCardStatus("frozen");

            ObjectNode frozen = mapper.createObjectNode();

            frozen.put("timestamp", command.getTimestamp());
            frozen.put("description",
                    "You have reached the minimum amount of funds, the card will be frozen");

            neededAccount.addTransaction(frozen);

            return;
        }

        if (neededAccount.getBalance() - neededAccount.getMinimumBalance()
                <= Utils.WARNING_LIMIT) {
            neededCard.setCardStatus("warning");
        }
    }
}
