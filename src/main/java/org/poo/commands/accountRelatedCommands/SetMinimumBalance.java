package org.poo.commands.accountRelatedCommands;

import lombok.Data;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;
import org.poo.user.User;

import java.util.ArrayList;

/**
 * Command to set the minimum balance.
 */
@Data
public final class SetMinimumBalance {
    /**
     * Utility class.
     */
    private SetMinimumBalance() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Execute the setMinimumBalance command.
     * @param command - the command to be executed
     * @param users - the list of users
     */
    public static void execute(final CommandInput command, final ArrayList<User> users) {
        User neededUser = null;
        Account neededAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    neededUser = user;
                    break;
                }
            }
        }

        if (neededUser == null) {
            return;
        }

        neededAccount.setMinimumBalance(command.getMinBalance());
    }
}
