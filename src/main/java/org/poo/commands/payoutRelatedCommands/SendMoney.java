package org.poo.commands.payoutRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.account.Account;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.CommandInput;
import org.poo.user.User;

import java.util.ArrayList;

/**
 * Send money command class.
 */
@Data
public final class SendMoney {
    /**
     * Utility class.
     */
    private SendMoney() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Execute the sendMoney command.
     * @param command - the command to be executed
     * @param users - the list of users
     */
    public static void execute(final CommandInput command, final ArrayList<User> users) {
        User sender = null;
        User receiver = null;

        Account senderAccount = null;
        Account receiverAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    sender = user;
                    senderAccount = account;
                }

                if (account.getAlias() != null
                    && account.getAlias().equals(command.getAccount())) {
                    return;
                }

                if (account.getAccountIBAN().equals(command.getReceiver())
                    || (account.getAlias() != null
                        && account.getAlias().equals(command.getReceiver()))) {
                    receiver = user;
                    receiverAccount = account;
                }
            }
        }

        if (sender == null || receiver == null) {
            return;
        }

        double exchangeRate;

        if (receiverAccount.getCurrency().equals(senderAccount.getCurrency())) {
            exchangeRate = 1.0;
        } else {
            exchangeRate = ExchangeRates.findCurrency(senderAccount.getCurrency(),
                                                      receiverAccount.getCurrency());
        }

        if (senderAccount.getBalance() < command.getAmount()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode transaction = mapper.createObjectNode();

            transaction.put("description", "Insufficient funds");
            transaction.put("timestamp", command.getTimestamp());

            sender.addTransaction(transaction);
            senderAccount.addTransaction(transaction);

            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode senderTransaction = mapper.createObjectNode();
        ObjectNode receiverTransaction = mapper.createObjectNode();

        senderTransaction.put("timestamp", command.getTimestamp());
        senderTransaction.put("description", command.getDescription());
        senderTransaction.put("senderIBAN", senderAccount.getAccountIBAN());
        senderTransaction.put("receiverIBAN", receiverAccount.getAccountIBAN());
        senderTransaction.put("amount",
                            command.getAmount() + " " + senderAccount.getCurrency());
        senderTransaction.put("transferType", "sent");

        sender.addTransaction(senderTransaction);
        senderAccount.addTransaction(senderTransaction);

        receiverTransaction.put("timestamp", command.getTimestamp());
        receiverTransaction.put("description", command.getDescription());
        receiverTransaction.put("senderIBAN", senderAccount.getAccountIBAN());
        receiverTransaction.put("receiverIBAN", receiverAccount.getAccountIBAN());
        receiverTransaction.put("amount",
                (command.getAmount() * exchangeRate) + " "
                        + receiverAccount.getCurrency());
        receiverTransaction.put("transferType", "received");

        receiver.addTransaction(receiverTransaction);
        receiverAccount.addTransaction(receiverTransaction);

        senderAccount.subtractAmountFromBalance(command.getAmount());
        receiverAccount.addAmountToBalance(command.getAmount() * exchangeRate);
    }
}
