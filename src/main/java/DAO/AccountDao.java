// You will need to design and create your own DAO classes from scratch. 
// You should refer to prior mini-project lab examples and course material for guidance.

package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Model.Account;
import Util.ConnectionUtil;

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

    public Account insertAccount(Account account){
        if (account.getUsername().length() >= 1
            && account.getPassword().length() >= 4
            /*&& !doesUsernameExist(account.getUsername())*/
            ){
                try (Connection conn = ConnectionUtil.getConnection()) {
                    String sql1 = "SELECT * FROM account WHERE username = ?";
                    PreparedStatement statement = conn.prepareStatement(sql1);
                    statement.setString(1, account.getUsername());
                    ResultSet resultSet = statement.executeQuery(); 

                    if (!resultSet.next()) {
                        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setString(1, account.getUsername());
                        preparedStatement.setString(2, account.getPassword());

                        preparedStatement.executeUpdate();
                        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
 
                        if(pkeyResultSet.next()){
                            int accountID = (int) pkeyResultSet.getLong(1); 
                            return new Account(accountID, account.getUsername(), account.getPassword());
                        }
                    }                 
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public Account loginAccount(Account account){ 
        try (Connection conn = ConnectionUtil.getConnection()) {
            String sql1 = "SELECT * FROM account WHERE username = ? and password = ?";
            PreparedStatement statement = conn.prepareStatement(sql1);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            ResultSet resultSet = statement.executeQuery(); 

            if (resultSet.next()) {
                int id = resultSet.getInt("account_id");
                String user = resultSet.getString("username");
                String pass = resultSet.getString("password");
                return new Account(id, user, pass);
            }            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}