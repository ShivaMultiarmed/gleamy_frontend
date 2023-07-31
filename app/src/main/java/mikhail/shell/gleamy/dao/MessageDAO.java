package mikhail.shell.gleamy.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.MsgInfo;

public class MessageDAO {
    private static MessageDAO msgDAO;
    public static final Map<Long, MsgInfo> infos = new HashMap<>();
    private MessageDAO ()
    {
        for (int i=0;i<5;i++)
        {
            infos.put((long)(i+1),new MsgInfo(i%2+1, i+1, i%2+1==1,"Brand new text", new Date()));
        }
    }
    public static MessageDAO getInstance()
    {
        if (msgDAO == null)
            msgDAO = new MessageDAO();
        return msgDAO;
    }
    public MsgInfo getMessage(long msgid)
    {
        return infos.get(msgid);
    }
    public Map<Long,MsgInfo> getAllMessages()
    {
        return infos;
    }
    public void removeMessage(long msgid)
    {
        infos.remove(msgid);
    }
    public void updateMessage(MsgInfo info) { infos.replace(info.msgid, info);}
    public long addMessage(MsgInfo info)
    {
        System.out.println("sending a msg");
        info.msgid = infos.entrySet().stream().reduce((first,second) -> second).get().getValue().msgid+1;
        infos.put(info.msgid,info);
        return info.msgid;
    }
}
