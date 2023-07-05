package Service;

import java.util.List;

import DAO.MessageDao;
import Model.Message;

public class MessageService{
    private MessageDao messageDao;

    public MessageService(){
        messageDao = new MessageDao();
    }

    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
    }

    public Message newMessage(Message message){
        return this.messageDao.newMessage(message);
    }

    public List<Message> getAllMessages(){
        return this.messageDao.getAllMessages();
    }

    public Message getMessageById(int messageId){
        return this.messageDao.getMessageById(messageId);
    }
}