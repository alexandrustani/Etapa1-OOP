package org.poo.commands.userRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;
import org.poo.account.Account;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Command to print transactions.
 */
@Data
public final class PrintTransactions {
    /**
     * Utility class.
     */
    private PrintTransactions() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Add user transactions to the transactions list.
     * @param accounts - the list of accounts
     * @param transactions - the list of transactions
     */
    private static void addUserTransactions(final ArrayList<Account> accounts,
                                            final ArrayList<ObjectNode> transactions) {
        for (Account account : accounts) {
            transactions.addAll(account.getAccountTransactions());
        }
    }
    /**
     * Execute the printTransactions command.
     * @param command - the command to be executed
     * @param users - the list of users
     * @param output - the output array
     */
    public static void execute(final CommandInput command,
                               final ArrayList<User> users, final ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandOutput = mapper.createObjectNode();

        commandOutput.put("command", "printTransactions");

        ArrayList<ObjectNode> transactions = new ArrayList<>();

        for (User user : users) {
            if (user.getEmail().equals(command.getEmail())) {
                addUserTransactions(user.getAccounts(), transactions);
            }
        }

        transactions.sort(Comparator.comparing(transaction -> transaction.get("timestamp")
                                                .asInt()));

        ArrayNode transactionsArray = mapper.createArrayNode();

        for (ObjectNode transaction : transactions) {
            transactionsArray.add(transaction);
        }

        commandOutput.set("output", transactionsArray);
        commandOutput.put("timestamp", command.getTimestamp());

        output.add(commandOutput);
    }
}
