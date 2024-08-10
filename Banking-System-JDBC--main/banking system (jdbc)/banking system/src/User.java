import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class User {
    private Connection con;
    private Scanner sc;

    public User(Connection connection, Scanner scanner){
        this.con = connection;
        this.sc = scanner;
    }

    public void register(){
        System.out.println();
        sc.nextLine();
        System.out.print("Full Name: ");
        String fullName = sc.nextLine();

        System.out.print("Email: ");
        String email = sc.nextLine();

        System.out.print("Password: ");
        String password = sc.nextLine();

        // checks if user is exsist or not
        if(user_exist(email)){
            System.out.println("User is Already Exsist For Thos Email....Try again Using Diffrent Email.");
            return;
        }

        String register_query = "INSERT INTO user (full_name, email, password) VALUES (?, ?, ?)";

        try {
            PreparedStatement preStmt = con.prepareStatement(register_query);
            preStmt.setString(1, fullName);
            preStmt.setString(2, email);
            preStmt.setString(3, password);

            int affectedRows = preStmt.executeUpdate();

            if(affectedRows > 0){
                System.out.println();
                System.out.println("Registration Successfull.");
            }
            else{
                System.out.println();
                System.out.println("Registration Failed. Try Again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login(){
        System.out.println();
        sc.nextLine();
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();
        String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";

        try {
            PreparedStatement preStmt = con.prepareStatement(login_query);
            preStmt.setString(1, email);
            preStmt.setString(2, password);
            ResultSet resultSet = preStmt.executeQuery();

            if(resultSet.next()){
                return email;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email){
        String query = "SELECT * FROM user WHERE email = ?";

        try {
            PreparedStatement preStmt = con.prepareStatement(query);
            preStmt.setString(1, email);
            ResultSet resultSet = preStmt.executeQuery();

            if( resultSet.next()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

}
