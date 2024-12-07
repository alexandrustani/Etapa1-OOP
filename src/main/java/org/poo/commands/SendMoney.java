package org.poo.commands;

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
                if (account.getAccountIBAN().equals(command.getReceiver())) {
                    receiver = user;
                    receiverAccount = account;
                }
            }
        }

        if (sender == null || receiver == null) {
            return;
        }

        Double exchangeRate = 0.0;

        if (receiverAccount.getCurrency().equals(senderAccount.getCurrency())) {
            exchangeRate = 1.0;
        } else {
            exchangeRate = ExchangeRates.findCurrency(senderAccount.getCurrency(),
                    receiverAccount.getCurrency());
        }

        if (senderAccount.getBalance() < command.getAmount() * exchangeRate) {
            return;
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        transaction.put("timestamp", command.getTimestamp());
        transaction.put("description", command.getDescription());
        transaction.put("senderIBAN", senderAccount.getAccountIBAN());
        transaction.put("receiverIBAN", receiverAccount.getAccountIBAN());
        transaction.put("amount",
                        String.format("%.1f %s", command.getAmount(),
                                        senderAccount.getCurrency()));
        transaction.put("transferType", "sent");

        sender.addTransaction(transaction);

        senderAccount.setBalance(senderAccount.getBalance() - command.getAmount());
        receiverAccount.setBalance(receiverAccount.getBalance() + command.getAmount() * exchangeRate);
    }
}
