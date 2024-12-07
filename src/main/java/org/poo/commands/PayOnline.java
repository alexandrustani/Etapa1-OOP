package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import java.util.Map;
import org.poo.user.User;

/**
 * Command to pay online.
 */

@Data
public final class PayOnline {
    /**
     * Utility class.
     */
    private PayOnline() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Execute the payOnline command.
     * @param command - the command to be executed
     * @param users - the list of users
     * @param output - the output array
     */
    public static void execute(final CommandInput command, final ArrayList<User> users,
                                final ArrayNode output) {
        Card neededCard = null;
        Account neededAccount = null;
        User neededUser = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        neededCard = card;
                        neededAccount = account;
                        neededUser = user;
                        break;
                    }
                }
            }
        }

        if (neededUser == null) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode commandNode = mapper.createObjectNode();

            commandNode.put("command", "payOnline");
            ObjectNode error = mapper.createObjectNode();
            error.put("description", "Card not found");
            error.put("timestamp", command.getTimestamp());
            commandNode.set("output", error);
            commandNode.put("timestamp", command.getTimestamp());

            output.add(commandNode);

            return;
        }

        Double neededExchangeRate;

        if (neededAccount.getCurrency().equals(command.getCurrency())) {
            neededExchangeRate = 1.0;
        } else {
            neededExchangeRate = ExchangeRates.findCurrency(command.getCurrency(),
                                                             neededAccount.getCurrency());
        }

        if (neededAccount.getBalance() < command.getAmount() * neededExchangeRate) {
            return;
        }

        if (neededCard.getCardType().equals("one-time")) {
            neededAccount.setBalance(neededAccount.getBalance()
                                     - command.getAmount() * neededExchangeRate);
            neededAccount.getCards().remove(neededCard);
            return;
        }

        neededAccount.setBalance(neededAccount.getBalance()
                                - command.getAmount() * neededExchangeRate);
    }
}
