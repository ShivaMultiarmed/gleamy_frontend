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

    public long getMsgid() {
        return msgid;
    }

    public void setMsgid(long msgid) {
        this.msgid = msgid;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}
