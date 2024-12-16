package org.poo.ebanking;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import org.poo.account.Account;
import org.poo.commands.accountRelatedCommands.AddInterest;
import org.poo.commands.accountRelatedCommands.AddAccount;
import org.poo.commands.accountRelatedCommands.ChangeInterestRate;
import org.poo.commands.accountRelatedCommands.DeleteAccount;
import org.poo.commands.accountRelatedCommands.SetMinimumBalance;
import org.poo.commands.accountRelatedCommands.SetAlias;
import org.poo.commands.accountRelatedCommands.Report;
import org.poo.commands.accountRelatedCommands.SpendingsReport;
import org.poo.commands.cardRelatedCommands.CheckCardStatus;
import org.poo.commands.cardRelatedCommands.CreateCard;
import org.poo.commands.cardRelatedCommands.DeleteCard;
import org.poo.commands.payoutRelatedCommands.AddFunds;
import org.poo.commands.payoutRelatedCommands.PayOnline;
import org.poo.commands.payoutRelatedCommands.SendMoney;
import org.poo.commands.payoutRelatedCommands.SplitPayment;
import org.poo.commands.userRelatedCommands.PrintTransactions;
import org.poo.commands.userRelatedCommands.PrintUsers;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.user.User;

import java.util.ArrayList;

import org.poo.utils.Utils;

/**
 * Ebanking class
 */
@Data
public final class Ebanking {
    private static ArrayList<User> users = new ArrayList<>();

    private Ebanking() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Create users from input
     * @param input - input object where I get the users
     */
    public static void createUsers(final ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) {
            User user = new User(input.getUsers()[i].getFirstName(),
                    input.getUsers()[i].getLastName(),
                    input.getUsers()[i].getEmail());
            users.add(user);
        }
    }

    /**
     * Get users and accounts
     * @param command - command to execute
     * @param neededAccounts - list of accounts that are needed
     */
    public static void getUsersAndAccounts(final CommandInput command,
                                           final ArrayList<Account> neededAccounts) {
        for (String accountIBAN : command.getAccounts()) {
            for (User user : users) {
                for (Account account : user.getAccounts()) {
                    if (account.getAccountIBAN().equals(accountIBAN)) {
                        neededAccounts.add(account);
                    }
                }
            }
        }
    }

    /**
     * System method that will be called from the main class
     * where I will call the methods necessary for my problem
     * @param input - input object
     * @param output - output object
     */
    public static void system(final ObjectInput input, final ArrayNode output) {
        createUsers(input);
        ExchangeRates.create(input);
        Utils.resetRandom();

        for (CommandInput command : input.getCommands()) {
            switch (command.getCommand()) {
                case "printUsers":
                    PrintUsers.execute(users, output, command.getTimestamp());
                    break;
                case "addAccount":
                    AddAccount.execute(command, users, command.getTimestamp());
                    break;
                case "createCard", "createOneTimeCard":
                    CreateCard.execute(command, users);
                    break;
                case "addFunds":
                    AddFunds.execute(command, users);
                    break;
                case "deleteAccount":
                    DeleteAccount.execute(command, users, output);
                    break;
                case "deleteCard":
                    DeleteCard.execute(command, users);
                    break;
                case "payOnline":
                    PayOnline.execute(command, users, output);
                    break;
                case "sendMoney":
                    SendMoney.execute(command, users);
                    break;
                case "printTransactions":
                    PrintTransactions.execute(command, users, output);
                    break;
                case "setMinBalance":
                    SetMinimumBalance.execute(command, users);
                    break;
                case "checkCardStatus":
                    CheckCardStatus.execute(command, users, output);
                    break;
                case "changeInterestRate":
                    ChangeInterestRate.execute(command, users, output);
                    break;
                case "splitPayment":
                    SplitPayment.execute(command);
                    break;
                case "addInterest":
                    AddInterest.execute(command, users, output);
                    break;
                case "report":
                    Report.execute(command, users, output);
                    break;
                case "spendingsReport":
                    SpendingsReport.execute(command, users, output);
                    break;
                case "setAlias":
                    SetAlias.execute(command, users);
                    break;
                default:
                    break;
            }
        }

        users.clear();
        ExchangeRates.reset();
    }
}
