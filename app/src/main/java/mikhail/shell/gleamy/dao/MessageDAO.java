package mikhail.shell.gleamy.dao;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.models.Message;

public class MessageDAO {
    private static MessageDAO msgDAO;
    public static final Map<Long, Message> infos = new HashMap<>();
    private MessageDAO ()
    {
        for (int i=0;i<5;i++)
        {
            //infos.put((long)(i+1),new Message(i%2+1, 1,i+1, i%2+1==1,"Brand new text", new LocalDateTime()));
        }
    }
    public static MessageDAO getInstance()
    {
        if (msgDAO == null)
            msgDAO = new MessageDAO();
        return msgDAO;
    }
    public Message getMessage(long msgid)
    {
        return infos.get(msgid);
    }
    public Map<Long, Message> getAllMessages()
    {
        return infos;
    }
    public void removeMessage(long msgid)
    {
        infos.remove(msgid);
    }
    public void updateMessage(Message info) { infos.replace(info.msgid, info);}
    public long addMessage(Message info)
    {
        System.out.println("sending a msg");
        info.msgid = infos.entrySet().stream().reduce((first,second) -> second).get().getValue().msgid+1;
        infos.put(info.msgid,info);
        return info.msgid;
    }
}
