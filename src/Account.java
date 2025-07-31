public class Account {
    int accountNumber;
    int pin;
    double balance;

    public Account(int accountNumber, int pin, double balance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return accountNumber + "\t" + pin + "\t" + balance;
    }
}
