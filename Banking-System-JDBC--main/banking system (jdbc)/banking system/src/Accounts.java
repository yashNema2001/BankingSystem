import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Accounts {
    private Connection con;
    private Scanner sc;

    public Accounts(Connection connection, Scanner scanner){
        this.con = connection;
        this.sc = scanner;
    }

    public long open_account(String email){
        if(!account_exist(email)){
            String query = "INSERT INTO accounts (account_number, full_name, email, balance, security_pin) VALUES (?, ?, ?, ?, ?)";
            sc.nextLine();
            System.out.print("Full Name: ");
            String full_name = sc.nextLine();
            System.out.print("Initial Ammount (Minimum Amount: 1500): ");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.print("Enter Security Pin: ");
            String security_pin = sc.nextLine();

            try {
                long account_number = generateAccountNumber();

                PreparedStatement preStmt = con.prepareStatement(query);
                preStmt.setLong(1, account_number);
                preStmt.setString(2, full_name);
                preStmt.setString(3, email);
                preStmt.setDouble(4, balance);
                preStmt.setString(5, security_pin);

                int affectedRow = preStmt.executeUpdate();

                if(affectedRow > 0){
                    return account_number;
                }
                else{
                    throw new RuntimeException("Account Creation failed.");
                }

            } catch (SQLException e) {
               e.printStackTrace();
            }
        }

        throw new RuntimeException("Account Already Exist.");
    }

    public long getAccountNumber(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";

        try {
            PreparedStatement preStmt = con.prepareStatement(query);
            preStmt.setString(1, email);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                return resultSet.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Account Number Doesn't Exist.");
    }

    private long generateAccountNumber(){
        try {
            Statement stmt = con.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");

            if(resultSet.next()){
                long last_account_number = resultSet.getLong("account_number");
                return last_account_number+1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 10000100;
    }

    public boolean account_exist(String email){
        String query = "SELECT account_number FROM accounts WHERE email = ?";

        try {
            PreparedStatement preStmt = con.prepareStatement(query);
            preStmt.setString(1, email);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
