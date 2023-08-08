package mikhail.shell.gleamy.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ChatInfo {

    private long id;
    @Setter(AccessLevel.NONE)
    private List<UserInfo> users;
    private String title;
    private MsgInfo last;
    public ChatInfo()
    {
        users = new ArrayList<>();
    }
    public ChatInfo(long id, String title, MsgInfo last)
    {
        this.id = id;
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
    public void addMember(UserInfo user)
    {
        if (users != null)
            users.add(user);
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
