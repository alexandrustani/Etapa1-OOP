# Project Assignment POO  - J. POO Morgan - Phase One

**Name**: Stanislav Alexandru
**Group**: 324CAb
---

## Packages Structure

### **Package: `account`**

This package contains the `Account` class, which represents a bank account.
Below is a breakdown of its main components:

- **`Account` Class**:  
  The base class for all other account types (Savings, Current). It includes
common characteristics shared by all accounts.
- **`SpecialAccountFunctions` Interface**:  
  Contains the default methods: `addCommerciant()`, `getACertainCommerciant()`,
and finally `addInterestRate()`, which are defined initially to return an
error if it used where I don't want it to be used.
- **`FactoryOfAccounts` Utility Class**:  
  Contains a static method `createAccount()` that returns an instance of an
account based on the type of account requested.(Factory Design Pattern)
- **`ClassicAccoun` Class**:  
  Which extends the `Account` class and implements the `SpecialAccountFunctions`
interface for commerciants. As we know this type of account defers from saving
account by the fact that I need to keep the commerciants for each `payOnline()`.
- **`SavingsAccount` Class**:  
  Which extends the `Account` class and implements the `SpecialAccountFunctions`
interface for adding the interest rate to the account balance.
- **`CommerciantDetails` Class**:  
  A simple class that contains the commerciant details, such as the name and
the amount of money that the a certain commerciant received.

### **Package: `card`**
This package just contains the `Card` class, which represents a bank card.
Nothing special here.

### **exchangeRates: `exchangeRates`**
This package contains the `ExchangeRates` class, which represents the exchange
rates between different currencies. This is much more like a Utility class,
which uses a Hashmap to store the exchange rates between different currencies and
important methods as:
- `create()` - to create the HashMap with the input exchange rates
- `reset()` - to reset the exchange rates for every test
- `findCurrency()` - which is bfs algorithm that returns the needed rate
between two currencies in the HashMap.

### **Package: `user`**
This package contains the `User` class, which represents a bank user.

### **Package: `ebanking`**
This package contains the `Ebanking` Utility class, which represents the
command line of my banking system. Here I have a static ArrayList of Users
and before the for of commands I get the Users from the input, the exchange
rates and I reset the random seed for the tests and after the for of commands
I reset the exchange rates and users. Here I have `getNeededAccount()` method,
which is used to create a list of accounts, needed for the splitPayment()
method.

### **Package: `commands`**
This package contains all of the commands that can be executed in the `system()`
method from the `Ebanking` class. Each command is represented by a Utility
class that contains a static method `execute()` that executes the command.
Honestly I don't have anything to say more special about every commands,
because they are explained in the homework assigment on the ocw website.
I will just mention some things that I consider interesting, such as:
- `PrintTransactions` - After getting the transactions of the user from
all it's accounts, I sort them by the timestamp and then print. I thought
it was easier to keep transactions as ObjectNodes.
- `SplitPayment` - To find the last account that hasn't enough money to
pay the split payment, I just started the for from the last account.

## How I developed the solution?
  Honestly it really helped me a lot to do the first Homework, because I
was already used to how to get the input and how to struct the project.
The most challenging part was for me the ExchangeRates class, because it
took me some time to implement the bfs algorithm in java correctly. It was
interesting to play with the static values and the loombooks and the hashmap
and sort implementations.

