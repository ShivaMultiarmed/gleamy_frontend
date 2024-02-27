package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class Chat implements Serializable {
    private long id;
    private List<User> users;
    private String title;
    private Message last;
    public Chat()
    {
        users = new ArrayList<>();
    }
    public Chat(long id, String title, Message last)
    {
        this();
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
    public void addMember(long userid)
    {
        users.add(new User(userid,"", "", ""));
    }
    public void addMember(User user)
    {
        if (users != null)
            users.add(user);
    }
    public void removeMember(long userid)
    {
        if (users != null)
            users.remove(userid);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Message getLast() {
        return last;
    }

    public void setLast(Message last) {
        this.last = last;
    }
    public boolean hasMember(long userid)
    {
        for (User user : users)
            if (user.getId() == userid)
                return true;
        return
                false;
    }
    public boolean hasMember(User user)
    {
        return hasMember(user.getId());
    }

    public void setUsers(List<User> users)
    {
        this.users = users;
    }
    public List<User> getUsers()
    {
        return users;
    }
}
