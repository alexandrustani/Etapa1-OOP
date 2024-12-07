package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import org.poo.user.User;

/**
 * Delete account command class.
 */
@Data
public final class DeleteAccount {
    /**
     * Utility class.
     */
    private DeleteAccount() {
        throw new IllegalStateException("Utility class");
    }

    public static void execute(final CommandInput command, final ArrayList<User> users,
                                final ArrayNode output) {
        User neededUser = null;
        Account neededAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    neededUser = user;
                    neededAccount = account;
                    break;
                }
            }
        }

        if (neededUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "deleteAccount");

        if (neededAccount.getBalance() == 0) {
            neededUser.getAccounts().remove(neededAccount);
            ObjectNode success = mapper.createObjectNode();
            success.put("success", "Account deleted");
            success.put("timestamp", command.getTimestamp());
            commandNode.set("output", success);
        }

        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);
    }
}
