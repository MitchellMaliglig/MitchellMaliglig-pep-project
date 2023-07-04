package Service;

import DAO.MessageDao;

public class MessageService{
    private MessageDao messageDao;

    public MessageService(){
        messageDao = new MessageDao();
    }

    public MessageService(MessageDao messageDao){
        this.messageDao = messageDao;
    }
}