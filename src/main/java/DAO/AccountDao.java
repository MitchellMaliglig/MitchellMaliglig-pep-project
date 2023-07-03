// You will need to design and create your own DAO classes from scratch. 
// You should refer to prior mini-project lab examples and course material for guidance.

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.Account;
import Util.ConnectionUtil;
import io.javalin.http.Context;

public class AccountDao{
    public void hi(){
        try (Connection conn = ConnectionUtil.getConnection()) {
        
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    private static boolean doesUsernameExist(String username){
        boolean accountExists = false;

        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                accountExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return accountExists;
    }

    public static void insertAccount(Context ctx){
        Account account = ctx.bodyAsClass(Account.class);
        System.out.println("here we are");
        if (account.getUsername().length() >= 1
            && account.getPassword().length() >= 4
            && !doesUsernameExist(account.getUsername())){
                try (Connection conn = ConnectionUtil.getConnection()) {
                    String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
                    PreparedStatement statement = conn.prepareStatement(sql);
                    statement.setString(1, account.getUsername());
                    statement.setString(2, account.getPassword());
                    statement.executeUpdate();
                } catch (SQLException e) {
                    e.printStackTrace();
                    ctx.status(500).result("Internal Server Error");
                    return;
                }
                ctx.status(200).result("OK");
        } else{
            ctx.status(400).result("Client error");
        }
    }
}