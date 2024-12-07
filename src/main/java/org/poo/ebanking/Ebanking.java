package org.poo.ebanking;

import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.Data;
import org.poo.commands.*;
import org.poo.exchangeRates.ExchangeRates;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.user.User;

import java.util.*;

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
    public static void createUsers(ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) {
            User user = new User(input.getUsers()[i].getFirstName(),
                    input.getUsers()[i].getLastName(),
                    input.getUsers()[i].getEmail());
            users.add(user);
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
                default:
                    break;
            }
        }

        users.clear();
        ExchangeRates.reset();
    }
}