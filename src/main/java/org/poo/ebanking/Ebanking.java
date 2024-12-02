package org.poo.ebanking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;
import org.poo.fileio.ObjectInput;
import org.poo.user.User;

import java.util.ArrayList;
import org.poo.account.Account;
import org.poo.card.Card;
import org.poo.utils.Utils;

public class Ebanking {
    ArrayList<User> users;

    public Ebanking() {
        this.users = new ArrayList<>();
    }

    public ArrayList<User> getUsers() {
        return this.users;
    }

    public void createUsers(ObjectInput input) {
        for (int i = 0; i < input.getUsers().length; i++) {
            User user = new User(input.getUsers()[i].getFirstName(),
                                 input.getUsers()[i].getLastName(),
                                 input.getUsers()[i].getEmail());
            this.users.add(user);
        }
    }

    public void printUsers(ArrayNode output, int timestamp) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode command = mapper.createObjectNode();

        command.put("command", "printUsers");

        ArrayNode users = mapper.createArrayNode();
        for (User user : this.users) {
            ObjectNode userNode = mapper.createObjectNode();

            userNode.put("firstName", user.getFirstName());
            userNode.put("lastName", user.getLastName());
            userNode.put("email", user.getEmail());

            ArrayNode accounts = mapper.createArrayNode();

            for (int i = 0; i < user.getAccounts().size(); i++) {
                ObjectNode account = mapper.createObjectNode();
                for (Account acc : user.getAccounts()) {
                    account.put("IBAN", acc.getIBAN());
                    account.put("balance", acc.getBalance());
                    account.put("currency", acc.getCurrency());
                    account.put("type", acc.getAccountType());
                    ArrayNode cards = mapper.createArrayNode();
                    for (Card card : acc.getCards()) {
                        ObjectNode cardNode = mapper.createObjectNode();
                        cardNode.put("cardNumber", card.getCardNumber());
                        cardNode.put("status", card.getStatus());
                        cards.add(cardNode);
                    }
                    account.put("cards", cards);
                }
                accounts.add(account);
            }
            userNode.set("accounts", accounts);

            users.add(userNode);
        }
        command.put("output", users);
        command.put("timestamp", timestamp);
        output.add(command);
    }

    public void addAccount(CommandInput command) {
        User neededUser = null;

        for (User user : this.users) {
            if (user.getEmail().equals(command.getEmail())) {
                neededUser = user;
                break;
            }
        }

        if (neededUser != null) {
            neededUser.getAccounts().add(new Account(command.getAccountType(),
                                                     command.getInterestRate(),
                                                     command.getCurrency()));
        }
    }

    public void deleteCard(CommandInput command) {
        User neededUser = null;
        Card neededCard = null;

        for (User user : this.users) {
            if (user.getEmail().equals(command.getEmail())) {
                neededUser = user;
                break;
            }
        }

        if (neededUser != null) {
            for (Account account : neededUser.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(command.getCardNumber())) {
                        neededCard = card;
                        break;
                    }
                }
            }
        }

        if (neededCard != null) {
            neededUser.getAccounts().getFirst().getCards().remove(neededCard);
        }
    }

    public void createCard(CommandInput command) {
        User neededUser = null;
        Account neededAccount = null;

        for (User user : this.users) {
            if (user.getEmail().equals(command.getEmail())) {
                neededUser = user;
                break;
            }
        }

        if (neededUser != null) {
            for (Account account : neededUser.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    break;
                }
            }
        }

        if (neededAccount != null) {
            if (command.getCommand().equals("createOneTimeCard")) {
                Card newCard = new Card("active", "oneTime");
                neededAccount.getCards().add(newCard);
            } else {
                Card newCard = new Card("active", "normal");
                neededAccount.getCards().add(newCard);
            }
        }
    }

    public void addFunds(CommandInput command) {
        Account neededAccount = null;

        for (User user : this.users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    break;
                }
            }
        }

        if (neededAccount != null) {
            neededAccount.setBalance(neededAccount.getBalance() + command.getAmount());
        }
    }

    public void deleteAccount(CommandInput command, ArrayNode output) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode commandNode = mapper.createObjectNode();

        commandNode.put("command", "deleteAccount");

        User neededUser = null;
        Account neededAccount = null;

        for (User user : this.users) {
            if (user.getEmail().equals(command.getEmail())) {
                neededUser = user;
                break;
            }
        }

        if (neededUser != null) {
            for (Account account : neededUser.getAccounts()) {
                if (account.getIBAN().equals(command.getAccount())) {
                    neededAccount = account;
                    break;
                }
            }
        }

        if (neededAccount != null) {
            neededUser.getAccounts().remove(neededAccount);
            ObjectNode success = mapper.createObjectNode();
            success.put("success", "Account deleted");
            success.put("timestamp", command.getTimestamp());
            commandNode.set("output", success);
        }

        commandNode.put("timestamp", command.getTimestamp());
        output.add(commandNode);
    }

    public void system(ObjectInput input, ArrayNode output) {
        createUsers(input);

        for (CommandInput command : input.getCommands()) {
            try {
                switch (command.getCommand()) {
                    case "printUsers":
                        printUsers(output, command.getTimestamp());
                        break;
                    case "addAccount":
                        addAccount(command);
                        break;
                    case "createCard", "createOneTimeCard":
                        createCard(command);
                        break;
                    case "addFunds":
                        addFunds(command);
                        break;
                    case "deleteAccount":
                        deleteAccount(command, output);
                        break;
                    case "deleteCard":
                        deleteCard(command);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Utils.resetRandom();
    }
}
