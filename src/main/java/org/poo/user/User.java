package org.poo.user;

import lombok.Getter;
import lombok.Setter;
import org.poo.account.Account;
import java.util.ArrayList;

/**
 * Class that represents the user.
 */
@Getter
@Setter
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<Account> accounts;

    /**
     * Constructor for User
     * @param firstName for the user
     * @param lastName for the user
     * @param email for the user
     */
    public User(final String firstName, final String lastName, final String email) {
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEmail(email);
        this.setAccounts(new ArrayList<>());
    }

    /**
     * Add account to user
     * @param account to add
     */
    public void addAccount(final Account account) {
        this.getAccounts().add(account);
    }
}
