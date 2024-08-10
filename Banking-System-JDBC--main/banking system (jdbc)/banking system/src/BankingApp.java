import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class BankingApp {
    // credentails of database
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String userName = "root";
    private static final String password = "H@rsh@29";

    public static void main(String[] args) {
        try {
            // loading the drivers from sql jar
            Class.forName("com.myssql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            // establish the connection with database
            Connection con = DriverManager.getConnection(url, userName, password);
            Scanner sc = new Scanner(System.in);
            User user = new User(con, sc);
            Accounts accounts = new Accounts(con, sc);
            AccountManager accountManager = new AccountManager(con, sc);

            String email;
            long account_number;

            while (true) {
                System.out.println("--------------------------------------------");
                System.out.println("WELCOME TO BANKING SYSTEM");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("--------------------------------------------");
                System.out.println();
                System.out.println("Enter Your Choice: ");
                int choice = sc.nextInt();

                switch (choice) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();

                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In");

                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("------------------------");
                                System.out.println("1. Open a new bank account");
                                System.out.println("2. Exit");
                                System.out.println("------------------------");
                                System.out.println();
                                System.out.println("Enter Choice: ");

                                if (sc.nextInt() == 1) {
                                    System.out.println();
                                    account_number = accounts.open_account(email);
                                    System.out.println();
                                    System.out.println("account created successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }
                            }

                            account_number = accounts.getAccountNumber(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("---------------------------");
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. LogOut");
                                System.out.println("---------------------------");
                                System.out.println();
                                System.out.println("Enter your choice: ");
                                choice2 = sc.nextInt();
                                System.out.println();

                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;

                                    default:
                                        System.out.println("Enter valid choice");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect Email Or Password");
                        }
                    case 3:
                        System.out.println();
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM.");
                        System.out.println("Exiting System...");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();    
        }
    }
}
