import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.Scanner;

public class AccountManager {
    private Connection con;
    private Scanner sc;

    public AccountManager(Connection con, Scanner sc) {
        this.con = con;
        this.sc = sc;
    }

    public void credit_money(long account_number) {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        System.out.println();

        try {
            con.setAutoCommit(false); // transaction handling

            if (account_number != 0) {
                PreparedStatement preStmt = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                preStmt.setLong(1, account_number);
                preStmt.setString(2, security_pin);
                ResultSet resultSet = preStmt.executeQuery();

                if (resultSet.next()) {

                    String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preStmt1 = con.prepareStatement(credit_query);
                    preStmt1.setDouble(1, amount);
                    preStmt1.setLong(2, account_number);

                    int affectedRow = preStmt1.executeUpdate();

                    if (affectedRow > 0) {
                        System.out.println("Rs. " + amount + " credited successfully");
                        con.commit();
                        con.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        con.rollback();
                        con.setAutoCommit(true);
                    }

                } else {
                    System.out.println("Invaild Pin Or Account Number!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void debit_money(long account_number) {
        sc.nextLine();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        System.out.println();

        try {
            con.setAutoCommit(false); // transaction handling

            if (account_number != 0) {
                PreparedStatement preStmt = con
                        .prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
                preStmt.setLong(1, account_number);
                preStmt.setString(2, security_pin);
                ResultSet resultSet = preStmt.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");

                    if (amount <= current_balance) {
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preStmt1 = con.prepareStatement(debit_query);
                        preStmt1.setDouble(1, amount);
                        preStmt1.setLong(2, account_number);

                        int affectedRow = preStmt1.executeUpdate();

                        if (affectedRow > 0) {
                            System.out.println("Rs. " + amount + " debited successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invaild Pin Or Account Number!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            con.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void getBalance(long account_number){
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        System.out.println();

        try {
            PreparedStatement preStmt = con.prepareStatement("SELECT balance FROM accounts WHERE account_number = ? AND security_pin = ?");
            preStmt.setLong(1, account_number);
            preStmt.setString(2, security_pin);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: " + balance);
            }
            else{
                System.out.println("Invaild Pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void transfer_money(long sender_account_number){
        sc.nextLine();
        System.out.print("Enter Reciver Account Number: ");
        long reciver_account_number = sc.nextLong();
        System.out.print("Enter Amount: ");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = sc.nextLine();
        System.out.println();

        try {
            con.setAutoCommit(false);

            if(sender_account_number != 0 && reciver_account_number != 0){
                PreparedStatement preStmt = con.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
                preStmt.setLong(1, sender_account_number);
                preStmt.setString(2, security_pin);
                ResultSet resultSet = preStmt.executeQuery();

                if(resultSet.next()){
                    double current_balance = resultSet.getDouble("balance");

                    if(amount<=current_balance){
                        String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
                        String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";

                        PreparedStatement cPreparedStatement = con.prepareStatement(credit_query);
                        PreparedStatement dPreparedStatement = con.prepareStatement(debit_query);
                        cPreparedStatement.setDouble(1, amount);
                        cPreparedStatement.setLong(2, reciver_account_number);
                        dPreparedStatement.setDouble(1, amount);
                        dPreparedStatement.setLong(2, sender_account_number);

                        int affectedRow1 = cPreparedStatement.executeUpdate();
                        int affectedRow2 = dPreparedStatement.executeUpdate();

                        if(affectedRow1 > 0 && affectedRow2 > 0){
                            System.out.println("Transaction Successfull");
                            System.out.println("Rs. "+amount+" transfered successfully");
                            con.commit();
                            con.setAutoCommit(true);
                            return;
                        } 
                        else{
                            System.out.println("Transaction failed");
                            con.rollback();
                            con.setAutoCommit(true);
                        }
                        
                    }
                    else{
                        System.out.println("Insufficient balance");
                    }
                }
                else{
                    System.out.println("Invalid security pin");
                }

            }
            else{
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            con.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
