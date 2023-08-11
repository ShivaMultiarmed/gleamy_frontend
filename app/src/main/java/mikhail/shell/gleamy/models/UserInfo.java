package mikhail.shell.gleamy.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
//import java.net.http.HttpClient;

@Getter @Setter
public class UserInfo implements Serializable {
    public long id;
    public String login, password;
    public UserInfo(String login, String password)
    {
        this.login = login;
        this.password = password;
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
}
