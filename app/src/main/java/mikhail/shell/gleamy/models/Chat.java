package mikhail.shell.gleamy.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Chat implements Serializable {
    private long id;
    private Map<Long, User> users;
    private String title;
    private Message last;
    public Chat()
    {
        users = new HashMap<>();
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
            users.put(userid, new User("", ""));
    }
    public void addMember(User user)
    {
        if (users != null)
            users.put(user.getId(), user);
    }
    public void removeMember(long userid)
    {
        if (users != null)
            users.remove(users.get(userid));
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
        return users.containsKey(userid);
    }

    public void setUsers(Map<Long, User> users)
    {
        this.users = users;
    }
    public Map<Long, User> getUsers()
    {
        return users;
    }
}
