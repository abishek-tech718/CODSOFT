import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

// Represents a single transaction
class Transaction {
    private String type;
    private double amount;
    private double balanceAfter;
    private Date timestamp;

    public Transaction(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = new Date();
    }

    @Override
    public String toString() {
        return String.format("  [%s] %-10s | Amount: Rs.%10.2f | Balance: Rs.%10.2f",
                timestamp.toString().substring(11, 19), type, amount, balanceAfter);
    }
}

// Represents the user's bank account
class BankAccount {
    private String accountNumber;
    private String accountHolder;
    private double balance;
    private List<Transaction> transactionHistory;

    public BankAccount(String accountNumber, String accountHolder, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
    }

    public double getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    // Deposit money into account
    public boolean deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  [ERROR] Deposit amount must be greater than zero.");
            return false;
        }
        if (amount > 200000) {
            System.out.println("  [ERROR] Maximum deposit per transaction is Rs.2,00,000.");
            return false;
        }
        balance += amount;
        transactionHistory.add(new Transaction("CREDIT", amount, balance));
        return true;
    }

    // Withdraw money from account
    public boolean withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  [ERROR] Withdrawal amount must be greater than zero.");
            return false;
        }
        if (amount % 100 != 0) {
            System.out.println("  [ERROR] Amount must be a multiple of Rs.100.");
            return false;
        }
        if (amount > 50000) {
            System.out.println("  [ERROR] Maximum withdrawal per transaction is Rs.50,000.");
            return false;
        }
        if (amount > balance) {
            System.out.printf("  [ERROR] Insufficient balance. Available balance: Rs.%.2f%n", balance);
            return false;
        }
        balance -= amount;
        transactionHistory.add(new Transaction("DEBIT", amount, balance));
        return true;
    }

    // Check and display balance
    public void checkBalance() {
        System.out.println("\n  +---------------------------------------+");
        System.out.println("  |         ACCOUNT BALANCE               |");
        System.out.println("  +---------------------------------------+");
        System.out.printf("  | Account Holder : %-20s |%n", accountHolder);
        System.out.printf("  | Account Number : %-20s |%n", maskAccount(accountNumber));
        System.out.printf("  | Available Bal  : Rs.%16.2f |%n", balance);
        System.out.println("  +---------------------------------------+");
    }

    // Display last 5 transactions
    public void printTransactionHistory() {
        System.out.println("\n  +-----------------------------------------------+");
        System.out.println("  |           RECENT TRANSACTIONS                 |");
        System.out.println("  +-----------------------------------------------+");
        if (transactionHistory.isEmpty()) {
            System.out.println("  | No transactions found.                        |");
        } else {
            int start = Math.max(0, transactionHistory.size() - 5);
            for (int i = start; i < transactionHistory.size(); i++) {
                System.out.println(transactionHistory.get(i));
            }
        }
        System.out.println("  +-----------------------------------------------+");
    }

    private String maskAccount(String acc) {
        if (acc.length() <= 4) return acc;
        return "XXXX-XXXX-" + acc.substring(acc.length() - 4);
    }
}

// ATM machine class
class ATM {
    private BankAccount account;
    private Scanner scanner;
    private static final String CORRECT_PIN = "1234";

    public ATM(BankAccount account) {
        this.account = account;
        this.scanner = new Scanner(System.in);
    }

    // Entry point
    public void start() {
        printWelcomeBanner();
        if (!authenticateUser()) {
            System.out.println("\n  [!] Card blocked after failed attempts. Contact your bank.");
            return;
        }
        showMainMenu();
        printGoodbyeMessage();
    }

    // PIN authentication with 3 attempts
    private boolean authenticateUser() {
        int attempts = 3;
        while (attempts > 0) {
            System.out.print("\n  Enter your PIN: ");
            String pin = scanner.nextLine().trim();
            if (pin.equals(CORRECT_PIN)) {
                System.out.println("\n  [OK] Authentication successful. Welcome, "
                        + account.getAccountHolder() + "!");
                return true;
            } else {
                attempts--;
                System.out.printf("  [!] Incorrect PIN. %d attempt(s) remaining.%n", attempts);
            }
        }
        return false;
    }

    // Main menu loop
    private void showMainMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            System.out.print("  Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleWithdraw();
                    break;
                case "2":
                    handleDeposit();
                    break;
                case "3":
                    account.checkBalance();
                    break;
                case "4":
                    account.printTransactionHistory();
                    break;
                case "5":
                    running = false;
                    break;
                default:
                    System.out.println("\n  [!] Invalid option. Please select 1-5.");
            }
        }
    }

    // Handle withdraw flow
    private void handleWithdraw() {
        System.out.println("\n  --- WITHDRAW ---");
        System.out.println("  (Multiples of Rs.100 | Max Rs.50,000 per transaction)");
        System.out.print("  Enter amount: Rs.");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (account.withdraw(amount)) {
                System.out.printf("%n  [OK] Rs.%.2f dispensed successfully!%n", amount);
                System.out.printf("  Remaining balance: Rs.%.2f%n", account.getBalance());
            }
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid amount entered.");
        }
    }

    // Handle deposit flow
    private void handleDeposit() {
        System.out.println("\n  --- DEPOSIT ---");
        System.out.println("  (Max Rs.2,00,000 per transaction)");
        System.out.print("  Enter amount: Rs.");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            if (account.deposit(amount)) {
                System.out.printf("%n  [OK] Rs.%.2f deposited successfully!%n", amount);
                System.out.printf("  New balance: Rs.%.2f%n", account.getBalance());
            }
        } catch (NumberFormatException e) {
            System.out.println("  [ERROR] Invalid amount entered.");
        }
    }

    private void printWelcomeBanner() {
        System.out.println("\n  ╔═════════════════════════════════════╗");
        System.out.println("  ║         NOVA BANK  ATM              ║");
        System.out.println("  ║       24/7 Banking Services         ║");
        System.out.println("  ╚═════════════════════════════════════╝");
        System.out.println("  Please insert your card. (Demo PIN: 1234)");
    }

    private void printMenu() {
        System.out.println("\n  +-------------------------------------+");
        System.out.println("  |            MAIN MENU                |");
        System.out.println("  +-------------------------------------+");
        System.out.println("  |  1.  Withdraw                       |");
        System.out.println("  |  2.  Deposit                        |");
        System.out.println("  |  3.  Check Balance                  |");
        System.out.println("  |  4.  Transaction History            |");
        System.out.println("  |  5.  Exit / Eject Card              |");
        System.out.println("  +-------------------------------------+");
    }

    private void printGoodbyeMessage() {
        System.out.println("\n  +-------------------------------------+");
        System.out.println("  |  Thank you for using NOVA BANK ATM  |");
        System.out.println("  |      Please collect your card.      |");
        System.out.println("  +-------------------------------------+\n");
    }
}

// Main class — entry point
public class ATM_Interface {
    public static void main(String[] args) {
        // Create a bank account with initial balance
        BankAccount userAccount = new BankAccount(
                "4821123456789012",
                "Arjun Sharma",
                25000.00
        );

        // Create the ATM and connect it to the account
        ATM atm = new ATM(userAccount);

        // Start the ATM
        atm.start();
    }
}