package org.poo.account;

import lombok.Data;
import org.poo.fileio.CommandInput;

/**
 * Factory class to create an account.
 */
@Data
public final class FactoryOfAccount {
    /**
     * Private constructor to avoid instantiation.
     */
    private FactoryOfAccount() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create an account based on the account type.
     *
     * @param accountType the type of account
     * @param command the command input
     * @return the account
     */
    public static Account createAccount(final String accountType, final CommandInput command) {
        return switch (accountType) {
            case "savings" -> new SavingsAccount(command.getCurrency(),
                                                        command.getInterestRate());
            case "classic" -> new ClassicAccount(command.getCurrency());
            default -> throw new IllegalArgumentException("Invalid account type");
        };
    }
}
