package org.poo.commands.payoutRelatedCommands;


import lombok.Data;
import org.poo.account.Account;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import org.poo.user.User;

/**
 * Add funds to an account.
 */
@Data
public final class AddFunds {
    /**
     * Utility class.
     */
    private AddFunds() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Executes the command.
     * @param command - the command to be executed
     * @param users - the list of users
     */
    public static void execute(final CommandInput command, final ArrayList<User> users) {
        Account neededAccount = null;

        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getAccountIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    break;
                }
            }
        }

        if (neededAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }

        neededAccount.addAmountToBalance(command.getAmount());
    }
}
