package org.poo.commands.userRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.poo.fileio.CommandInput;
import org.poo.user.User;

import java.util.ArrayList;

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

        for (User user : users) {
            if (user.getEmail().equals(command.getEmail())) {
                ArrayNode transactions = mapper.createArrayNode();
                for (ObjectNode transaction : user.getTransactions()) {
                    transactions.add(transaction);
                }
                commandOutput.set("output", transactions);
                commandOutput.put("timestamp", command.getTimestamp());
                output.add(commandOutput);
                return;
            }
        }
    }
}
