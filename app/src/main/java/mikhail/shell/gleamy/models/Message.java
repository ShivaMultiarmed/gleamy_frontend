package mikhail.shell.gleamy.models;

import android.os.Build;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalTime;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Message implements Serializable {
    public long msgid,userid,chatid;
    public boolean isMine;
    public String text, login;
    public LocalDateTime datetime;
    public Message(long userid, long chatid, long msgid, boolean isMine, String text)
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
        return datetime;
    }

    public void setDateTime(LocalDateTime datetime) {
        this.datetime = datetime;
    }
    public long getChatid() {
        return chatid;
    }

    public void setChatid(long chatid) {
        this.chatid = chatid;
    }
    public String generateTimeString()
    {
        StringBuilder builder = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && datetime != null) {
            LocalTime time = datetime.toLocalTime();
            if (time.getHour() < 10)
                builder.append(0);
            builder.append(time.getHour());
            builder.append(":");
            if (time.getMinute() < 10)
                builder.append(0);
            builder.append(time.getMinute());
        }
        return builder.toString();
    }
}
