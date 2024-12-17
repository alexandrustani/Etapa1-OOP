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
     * @param mapper - the object mapper
     */
    public static void execute(final CommandInput command, final ArrayList<User> users,
                                final ObjectMapper mapper) {
        Account senderAccount = null;
        Account receiverAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    senderAccount = account;
                }

                if (account.getAlias() != null
                    && account.getAlias().equals(command.getAccount())) {
                    return;
                }

                if (account.getAccountIBAN().equals(command.getReceiver())
                    || (account.getAlias() != null
                        && account.getAlias().equals(command.getReceiver()))) {
                    receiverAccount = account;
                }
            }
        }

        if (senderAccount == null || receiverAccount == null) {
            return;
        }

        double exchangeRate = ExchangeRates.findCurrency(senderAccount.getCurrency(),
                                                         receiverAccount.getCurrency());

        if (senderAccount.getBalance() < command.getAmount()) {
            ObjectNode transaction = mapper.createObjectNode();

            transaction.put("description", "Insufficient funds");
            transaction.put("timestamp", command.getTimestamp());

            senderAccount.addTransaction(transaction);

            return;
        }

        ObjectNode senderTransaction = mapper.createObjectNode();
        ObjectNode receiverTransaction = mapper.createObjectNode();

        senderTransaction.put("timestamp", command.getTimestamp());
        senderTransaction.put("description", command.getDescription());
        senderTransaction.put("senderIBAN", senderAccount.getAccountIBAN());
        senderTransaction.put("receiverIBAN", receiverAccount.getAccountIBAN());
        senderTransaction.put("amount",
                            command.getAmount() + " " + senderAccount.getCurrency());
        senderTransaction.put("transferType", "sent");

        senderAccount.addTransaction(senderTransaction);

        receiverTransaction.put("timestamp", command.getTimestamp());
        receiverTransaction.put("description", command.getDescription());
        receiverTransaction.put("senderIBAN", senderAccount.getAccountIBAN());
        receiverTransaction.put("receiverIBAN", receiverAccount.getAccountIBAN());
        receiverTransaction.put("amount",
                (command.getAmount() * exchangeRate) + " "
                        + receiverAccount.getCurrency());
        receiverTransaction.put("transferType", "received");

        receiverAccount.addTransaction(receiverTransaction);

        senderAccount.subtractAmountFromBalance(command.getAmount());
        receiverAccount.addAmountToBalance(command.getAmount() * exchangeRate);
    }
}
