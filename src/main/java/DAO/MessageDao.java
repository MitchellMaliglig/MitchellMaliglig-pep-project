package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDao {
    public Message newMessage(Message message){
        if (message.getMessage_text().length() > 0
            && message.getMessage_text().length() < 255
            ){
                try (Connection conn = ConnectionUtil.getConnection()) {
                    String sql1 = "SELECT * FROM account WHERE account_id = ?";
                    PreparedStatement statement = conn.prepareStatement(sql1);
                    statement.setInt(1, message.getPosted_by());
                    ResultSet resultSet = statement.executeQuery(); 

                    if (resultSet.next()) {
                        String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
                        PreparedStatement preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setInt(1, message.getPosted_by());
                        preparedStatement.setString(2, message.getMessage_text());
                        preparedStatement.setLong(3, message.getTime_posted_epoch());

                        preparedStatement.executeUpdate();
                        ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
                        if(pkeyResultSet.next()){
                            int generated_message_id = (int) pkeyResultSet.getLong(1);
                            return new Message(generated_message_id, message.getPosted_by(), 
                            message.getMessage_text(), message.getTime_posted_epoch());
                        }      
                    }       
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return null;
    }

    public List<Message> getAllMessages(){
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM message";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public Message getMessageById(int messageId){
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                return new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int messageId){
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, messageId);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()){
                Message message = new Message(rs.getInt("message_id"), 
                    rs.getInt("posted_by"), 
                    rs.getString("message_text"), 
                    rs.getLong("time_posted_epoch"));
                String sql2 = "DELETE FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
                preparedStatement2.setInt(1, messageId);
                preparedStatement2.executeUpdate();
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message updateMessageById(int messageId, String newMessageText){
        if (newMessageText.length() > 0 && newMessageText.length() < 255){
            try (Connection conn = ConnectionUtil.getConnection()){
                String sql = "SELECT * FROM message WHERE message_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1, messageId);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()){
                    String sql2 = "UPDATE message SET message_text = ? WHERE message_id = ?";
                    PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
                    preparedStatement2.setString(1, newMessageText);
                    preparedStatement2.setInt(2, messageId);
                    preparedStatement2.executeUpdate();
                    return new Message(rs.getInt("message_id"), 
                        rs.getInt("posted_by"), 
                        newMessageText, 
                        rs.getLong("time_posted_epoch"));
                }
            }catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    public List<Message> getAllMessagesGivenId(int accountId){  
        List<Message> messages = new ArrayList<>();
        try (Connection conn = ConnectionUtil.getConnection()){
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, accountId);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
                messages.add(message);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
