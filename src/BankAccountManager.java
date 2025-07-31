import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * A Java program that manages bank accounts using file I/O, HashMap, and ArrayList.
 * Features include withdraw, deposit, account creation, PIN authentication, admin tools, and session persistence.
 */
public class BankAccountManager {
    public static void main(String[] args) throws FileNotFoundException {
        HashMap<Integer, Account> accounts = new HashMap<>();
        ArrayList<Account> accountList = new ArrayList<>();

        // Load account data from file
        Scanner filescan = new Scanner(new File("initialBankWithPin.txt"));
        while (filescan.hasNextInt()) {
            int accountNumber = filescan.nextInt();
            int pin = filescan.nextInt();
            double balance = filescan.nextDouble();
 Account acc = new Account(accountNumber, pin, balance);
accounts.put(accountNumber, acc);
accountList.add(acc);
        }
        filescan.close();

        Scanner scan = new Scanner(System.in);

        // Main login/session loop
        while (true) {
            System.out.print("Enter your account number (-1 to exit): ");
            int accountNumber = scan.nextInt();

            if (accountNumber == -1) {
                System.out.println("Have a wonderful day!");
                break;
            }

            // Admin login check
            if (accountNumber == 0) {
                System.out.print("Enter Admin PIN: ");
                int adminPin = scan.nextInt();
                if (adminPin == 0000) {
                    runAdminMode(accounts, accountList, scan);
                } else {
                    System.out.println("Incorrect Admin PIN.");
                }
                continue;
            }

            // Regular user account access
            Account account = accounts.get(accountNumber);
            if (account == null) {
                System.out.println("Error: Account number does not exist.");
                continue;
            }

            if (!authenticate(account, scan)) {
                continue;
            }

            // User session menu
            boolean running = true;
            while (running) {
                int choice = menu(scan);
                switch (choice) {
                    case 1:
                        withdraw(account, accounts, accountList, scan);
                        break;
                    case 2:
                        deposit(account, accounts, accountList, scan);
                        break;
                    case 3:
                        checkBalance(account);
                        break;
                    case 4:
                        running = false;
                        break;
                    case 5:
                        createAccount(accounts, accountList, scan);
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }

        // Save session changes to output file
        PrintWriter printWriter = new PrintWriter("modifiedBankWithPin.txt");
        printAccounts(accountList, printWriter);
        printWriter.flush();
        printWriter.close();
        scan.close();

        // Overwrite the initial file with updated content
       // Copy modifiedBankWithPin.txt -> initialBankWithPin.txt
try (
    Scanner modifiedReader = new Scanner(new File("modifiedBankWithPin.txt"));
    PrintWriter initialWriter = new PrintWriter("initialBankWithPin.txt")
) {
    while (modifiedReader.hasNextLine()) {
        initialWriter.println(modifiedReader.nextLine());
    }
}

// Wipe contents of modifiedBankWithPin.txt
new PrintWriter("modifiedBankWithPin.txt").close();
    }

    /**
     * Displays the menu options and returns user's selected choice.
     */
    public static int menu(Scanner sc) {
        System.out.println("Menu:");
        System.out.println("1. Withdraw");
        System.out.println("2. Deposit");
        System.out.println("3. Check Balance");
        System.out.println("4. Exit");
        System.out.println("5. Create New Account");
        System.out.print("Enter your choice: ");
        return sc.nextInt();
    }

    /**
     * Handles withdrawing money from an account.
     */
    public static void withdraw(Account account, HashMap<Integer, Account> accounts, ArrayList<Account> accountList, Scanner sc) {
        System.out.print("Enter amount to withdraw: ");
        double amount = sc.nextDouble();
        if (amount <= account.balance) {
            account.balance -= amount;
            System.out.println("Withdrawal successful. New balance: $" + account.balance);
            if (amount > 0) {
                updateAccountInList(account, accountList);
            }
        } else {
            System.out.println("Error: Insufficient funds.");
        }
    }

    /**
     * Handles depositing money into an account.
     */
    public static void deposit(Account account, HashMap<Integer, Account> accounts, ArrayList<Account> accountList, Scanner sc) {
        System.out.print("Enter amount to deposit: ");
        double amount = sc.nextDouble();
        account.balance += amount;
        System.out.println("Deposit successful. New balance: $" + account.balance);
        if (amount > 0) {
            updateAccountInList(account, accountList);
        }
    }

    /**
     * Displays the current account balance.
     */
    public static void checkBalance(Account account) {
        System.out.println("Current balance: $" + account.balance);
    }

    /**
     * Prints account details to the provided writer (used to save file).
     */
    public static void printAccounts(ArrayList<Account> accountList, PrintWriter pw) {
for (Account account : accountList) {
    pw.println(account);
}
    }

    /**
     * Replaces an existing account in the list with its updated version.
     */
    public static void updateAccountInList(Account updatedAccount, ArrayList<Account> accountList) {
        for (int i = 0; i < accountList.size(); i++) {
            Account account = accountList.get(i);
            if (account.accountNumber == updatedAccount.accountNumber) {
                accountList.set(i, updatedAccount);
                break;
            }
        }
    }

    /**
     * Creates a new account by prompting for info and adding to structures.
     */
    public static void createAccount(HashMap<Integer, Account> accounts, ArrayList<Account> accountList, Scanner sc) {
        System.out.print("Enter new account number: ");
        int newAccountNumber = sc.nextInt();

        if (accounts.containsKey(newAccountNumber)) {
            System.out.println("Error: Account number already exists.");
            return;
        }

        System.out.print("Enter a 4-digit PIN: ");
        int newPIN = sc.nextInt();

        System.out.print("Enter initial deposit: ");
        double initialDeposit = sc.nextDouble();

Account newAccount = new Account(newAccountNumber, newPIN, initialDeposit);
accounts.put(newAccountNumber, newAccount);
accountList.add(newAccount);

        System.out.println("Account successfully created.");
    }

    /**
     * Verifies the user's PIN with up to 3 attempts.
     */
    public static boolean authenticate(Account account, Scanner sc) {
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Enter your PIN: ");
            int pinAttempt = sc.nextInt();
            if (pinAttempt == account.pin) return true;
            attempts++;
            System.out.println("Incorrect PIN. Attempt " + attempts + " of 3.");
        }
        System.out.println("Too many failed attempts. Returning to main menu.");
        return false;
    }

    /**
     * Admin-only menu for managing accounts: view, reset, delete, and stats.
     */
    public static void runAdminMode(HashMap<Integer, Account> accounts, ArrayList<Account> accountList, Scanner sc) {
        boolean adminRunning = true;
        while (adminRunning) {
            System.out.println("\n Admin Menu:");
            System.out.println("1. View All Accounts");
            System.out.println("2. Reset User Balance");
            System.out.println("3. Delete Account");
            System.out.println("4. Show Account Stats");
            System.out.println("5. Exit Admin Mode");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
    for (Account acc : accounts.values()) {
        System.out.println(acc.accountNumber + "\t$" + acc.balance);
    }
    break;


                case 2:
                    System.out.print("Enter account number to reset: ");
                    int resetAcc = sc.nextInt();
                    Account accToReset = accounts.get(resetAcc);
                    if (accToReset != null) {
                        accToReset.balance = 0;
                        updateAccountInList(accToReset, accountList);
                        System.out.println("Balance reset to $0.");
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 3:
                    System.out.print("Enter account number to delete: ");
                    int delAcc = sc.nextInt();
                    if (accounts.containsKey(delAcc)) {
                        accounts.remove(delAcc);
                        accountList.removeIf(a -> a.accountNumber == delAcc);
                        System.out.println("Account deleted.");
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;

                case 4:
    if (accounts.isEmpty()) {
        System.out.println("No accounts available.");
        break;
    }
    double sum = 0, min = Double.MAX_VALUE, max = Double.MIN_VALUE;
    for (Account acc : accounts.values()) {
        sum += acc.balance;
        if (acc.balance < min) min = acc.balance;
        if (acc.balance > max) max = acc.balance;
    }
    System.out.printf("Accounts: %d\nMin Balance: $%.2f\nMax Balance: $%.2f\nAverage: $%.2f\n",
            accounts.size(), min, max, sum / accounts.size());
    break;

                case 5:
                    adminRunning = false;
                    break;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
