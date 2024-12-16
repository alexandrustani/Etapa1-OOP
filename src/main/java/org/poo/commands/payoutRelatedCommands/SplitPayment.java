package org.poo.commands.payoutRelatedCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.poo.account.Account;
import org.poo.ebanking.Ebanking;
import org.poo.fileio.CommandInput;

import java.util.ArrayList;
import org.poo.exchangeRates.ExchangeRates;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Split payment command class.
 */
@Data
public final class SplitPayment {
    /**
     * Utility class.
     */
    private SplitPayment() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Check if all the account can pay the amount.
     * @param accounts - the list of accounts
     * @param amountPerAccount - the amount to be paid
     * by the accounts
     * @return the Account with insufficient funds
     */
    public static Account canPay(final ArrayList<Account> accounts, final Double amountPerAccount,
                                 final ArrayList<Double> exchangeRates) {
        Account insufficientFunds = null;

        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getBalance() < amountPerAccount * exchangeRates.get(i)) {
                insufficientFunds = accounts.get(i);
            }
        }

        return insufficientFunds;
    }

    /**
     * Execute the splitPayment command.
     * @param command - the command to be executed
     */
    public static void execute(final CommandInput command) {
        ArrayList<Account> accountsToPay = new ArrayList<>();

        double neededAmountPerAccount = command.getAmount() / command.getAccounts().size();

        ArrayList<Double> exchangeRates = new ArrayList<>();

        Ebanking.getUsersAndAccounts(command, accountsToPay);

        for (Account account : accountsToPay) {
            if (account.getCurrency().equals(command.getCurrency())) {
                exchangeRates.add(1.0);
            } else {
                exchangeRates.add(ExchangeRates.findCurrency(command.getCurrency(),
                        account.getCurrency()));
            }
        }

        Account insufficientFunds = canPay(accountsToPay, neededAmountPerAccount, exchangeRates);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();

        transaction.put("amount", neededAmountPerAccount);
        transaction.put("currency", command.getCurrency());
        transaction.put("description", String.format("Split payment of %.2f %s",
                command.getAmount(), command.getCurrency()));
        transaction.set("involvedAccounts", mapper.valueToTree(command.getAccounts()));
        transaction.put("timestamp", command.getTimestamp());

        if (insufficientFunds != null) {
            transaction.put("error",
                            String.format("Account %s has insufficient funds for a split payment.",
                                            insufficientFunds.getAccountIBAN()));

            for (Account account : accountsToPay) {
                account.addTransaction(transaction);
            }

            return;
        }

        for (int i = 0; i < accountsToPay.size(); i++) {
            accountsToPay.get(i).subtractAmountFromBalance(neededAmountPerAccount
                                                            * exchangeRates.get(i));

            accountsToPay.get(i).addTransaction(transaction);
        }
    }
}
