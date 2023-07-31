package mikhail.shell.gleamy.models;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgInfo {
    public long msgid, userid;
    public boolean isMine;
    public String text;
    public Date datetime;
    public MsgInfo(long userid, long msgid, boolean isMine,String text, Date datetime)
    {
        this.userid = userid;
        this.msgid = msgid;
        this.text = text;
        this.datetime = datetime;
    }
}
