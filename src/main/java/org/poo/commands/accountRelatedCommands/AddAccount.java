package org.poo.commands.accountRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;
import org.poo.account.FactoryOfAccount;

import java.util.ArrayList;

/**
 * Add account to user
 */
@Data
public final class AddAccount {
    /**
     * Utility class
     */
    private AddAccount() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Add account to user
     * @param command to execute
     * @param users to add account
     */
    public static void execute(final CommandInput command, final ArrayList<User> users,
                                final int timestamp) {
        User neededUser = null;

        for (User user : users) {
            if (user.getEmail().equals(command.getEmail())) {
                neededUser = user;
                break;
            }
        }

        if (neededUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        neededUser.addAccount(FactoryOfAccount.createAccount(command.getAccountType(),
                command));

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        transaction.put("timestamp", timestamp);
        transaction.put("description", "New account created");

        neededUser.addTransaction(transaction);
        neededUser.getAccounts().getLast().addTransaction(transaction);
    }
}
