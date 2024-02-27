package mikhail.shell.gleamy.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
//import java.net.http.HttpClient;

@Getter @Setter
public class User implements Serializable {
    public Long id;
    public String login, password, email, avatar;
    public User(String login, String password, String email)
    {
        this.login = login;
        this.password = password;
        this.email = email;
    }
    public User(long id, String login, String password, String email)
    {
        this(login, password, email);
        this.id = id;
    }
    public User(long id)
    {
        setId(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }
    public  String getAvatar()
    {
        return avatar;
    }
}
