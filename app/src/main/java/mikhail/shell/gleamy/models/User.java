package mikhail.shell.gleamy.models;

import lombok.Getter;
import lombok.Setter;
//import java.net.http.HttpClient;

@Getter @Setter
public class User {
    public long id;
    public String login, password;
    public User(String login, String password)
    {
        this.login = login;
        this.password = password;
    }
}
