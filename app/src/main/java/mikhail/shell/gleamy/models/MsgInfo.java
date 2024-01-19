package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MsgInfo implements Serializable {
    public long msgid,userid,chatid;
    public boolean isMine;
    public String text, login;
    public LocalDateTime dateTime;
    public MsgInfo(long userid, long chatid, long msgid, boolean isMine,String text)
    {
        this.userid = userid;
        this.msgid = msgid;
        this.text = text;
        this.chatid = chatid;
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
    public long getChatid() {
        return chatid;
    }

    public void setChatid(long chatid) {
        this.chatid = chatid;
    }
}
