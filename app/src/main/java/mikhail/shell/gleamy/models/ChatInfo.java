package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChatInfo implements Serializable {

    private long id;
    @Setter(AccessLevel.PRIVATE)
    private final Map<Long, UserInfo> users;
    private String title;
    private MsgInfo last;
    public ChatInfo()
    {
        users = new HashMap<>();
    }
    public ChatInfo(long id, String title, MsgInfo last)
    {

        this.id = id;
        users = new HashMap<>();
        this.title = title;
        this.last = last;

    }
    public long getId() {
        return id;
    }
    public void setId(long id)
    {
        if (this.id == 0)
            this.id = id;
    }
    public void addMember(long userid)
    {
            users.put(userid, new UserInfo("", ""));
    }
    public void addMember(UserInfo user)
    {
        if (users != null)
            users.put(user.getId(), user);
    }
    public void removeMember(UserInfo user)
    {
        if (users != null)
            users.remove(user);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MsgInfo getLast() {
        return last;
    }

    public void setLast(MsgInfo last) {
        this.last = last;
    }
}
