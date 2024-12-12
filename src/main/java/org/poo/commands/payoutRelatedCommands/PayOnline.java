package org.poo.commands.payoutRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.account.Account;
import org.poo.account.ClassicAccount;
import org.poo.card.Card;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;

import org.poo.account.CommerciantsDetails;
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
     * Create the transactions for an error.
     * @param command - the command to be executed
     * @param neededUser - the user that made the payment
     * @param neededCard - the card used for the payment
     * @param output - the output array
     * @param neededAccount - the account from which the payment is made
     * @return true if an error occurred, false otherwise
     */
    public static boolean createErrorTransactions(final CommandInput command,
                                                  final User neededUser,
                                                  final Card neededCard,
                                                  final ArrayNode output,
                                                  final Account neededAccount) {
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

            return true;
        } else if (neededCard.getCardStatus().equals("frozen")) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode transaction = mapper.createObjectNode();

            transaction.put("timestamp", command.getTimestamp());
            transaction.put("description", "The card is frozen");

            neededAccount.addTransaction(transaction);
            neededUser.addTransaction(transaction);

            return true;
        }

        return false;
    }

    /**
     * Create the transactions for a successful payment.
     * @param command - the command to be executed
     * @param neededUser - the user that made the payment
     * @param neededAccount - the account from which the payment is made
     * @param neededCard - the card used for the payment
     * @param neededExchangeRate - the exchange rate between the account currency
     *                              and the payment currency
     */
    public static void createSuccesTransactions(final CommandInput command, final User neededUser,
                                                final Account neededAccount,
                                                final Card neededCard,
                                                final double neededExchangeRate) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        if (neededAccount.getBalance() < command.getAmount() * neededExchangeRate) {
            transaction.put("description", "Insufficient funds");
            transaction.put("timestamp", command.getTimestamp());

            neededUser.addTransaction(transaction);
            neededAccount.addTransaction(transaction);

            return;
        }

        transaction.put("amount", command.getAmount() * neededExchangeRate);
        transaction.put("commerciant", command.getCommerciant());
        transaction.put("description", "Card payment");
        transaction.put("timestamp", command.getTimestamp());

        neededUser.addTransaction(transaction);
        neededAccount.addTransaction(transaction);

        CommerciantsDetails commerciant = new CommerciantsDetails(command.getCommerciant(),
                command.getAmount()
                        * neededExchangeRate);
        if (neededAccount.getAccountType().equals("classic")) {
            ((ClassicAccount) neededAccount).addCommerciant(commerciant);
        }

        neededAccount.setBalance(neededAccount.getBalance() - command.getAmount()
                                 * neededExchangeRate);

        if (neededCard.getCardType().equals("one-time")) {
            ObjectNode transaction1 = mapper.createObjectNode();

            transaction1.put("account", neededAccount.getAccountIBAN());
            transaction1.put("card", neededCard.getCardNumber());
            transaction1.put("cardHolder", neededUser.getEmail());
            transaction1.put("description", "The card has been destroyed");
            transaction1.put("timestamp", command.getTimestamp());

            neededAccount.addTransaction(transaction1);
            neededUser.addTransaction(transaction1);

            neededAccount.generateNewCardNumber(neededCard);

            ObjectNode transaction2 = mapper.createObjectNode();

            transaction2.put("account", neededAccount.getAccountIBAN());
            transaction2.put("card", neededAccount.getCards().getLast().getCardNumber());
            transaction2.put("cardHolder", neededUser.getEmail());
            transaction2.put("description", "New card created");
            transaction2.put("timestamp", command.getTimestamp());

            neededAccount.addTransaction(transaction2);
            neededUser.addTransaction(transaction2);
        }
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

        if (createErrorTransactions(command, neededUser, neededCard, output, neededAccount)) {
            return;
        }

        double neededExchangeRate;

        if (neededAccount.getCurrency().equals(command.getCurrency())) {
            neededExchangeRate = 1.0;
        } else {
            neededExchangeRate = ExchangeRates.findCurrency(command.getCurrency(),
                                                             neededAccount.getCurrency());
        }

        createSuccesTransactions(command, neededUser, neededAccount, neededCard,
                                 neededExchangeRate);
    }
}
