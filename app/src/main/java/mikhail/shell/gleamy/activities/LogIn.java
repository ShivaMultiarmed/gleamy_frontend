package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//import java.net.http.HttpClient;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AppHttpClient;
import mikhail.shell.gleamy.api.AuthAPIClient;
import mikhail.shell.gleamy.models.UserInfo;
public class LogIn extends AppCompatActivity {


    private AppHttpClient httpClient;
    private AuthAPIClient authAPIClient;
    private enum Status {
        OK, NOTFOUND, PASSINCORRECT, EMPTY
    }

    private EditText login, password;
    private Button btn;
    private TextView msgView;
    private List<UserInfo> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        users = new ArrayList<>();
        users.add(new UserInfo("James","witch"));
        users.add(new UserInfo("Ashley","dog"));
        users.add(new UserInfo("Mark","car"));
        for (UserInfo u:users)
            System.out.println(u.login);
        initHttp();
        init();
        initBtn();
    }
    private void initHttp()
    {
        httpClient = AppHttpClient.getClient();
        authAPIClient = AuthAPIClient.getClient();
    }
    private void init()
    {

        login = findViewById(R.id.logInName);
        password = findViewById(R.id.logInPassword);
        msgView = findViewById(R.id.logInMessage);




    }
    private void initBtn()
    {
        btn = findViewById(R.id.logInBtn);
        btn.setOnClickListener(e->{
            String status = validate(login.getText().toString(), password.getText().toString());
            displayMessage(status);
        });
    }
    private String validate(String login, String password)
    {
        if (login.equals("") || password.equals(""))
            return "EMPTY";
        else
            return authAPIClient.login(login, password);
    }

    private UserInfo getUser(String login)
    {
        UserInfo user = null;
        for (UserInfo u:users)
            if (Objects.equals(u.login, login))
                user = u;
        return user;
    }
    private void displayMessage(String status)
    {
        String msg = null;
        switch (status)
        {
            case "OK": msg = "Вы успешно вошли";
                break;
            case "PASSINCORRECT": msg = "Неверный пароль";
                break;
            case "NOTFOUND": msg = "Пользователь не найден";
                break;
            case "EMPTY": msg = "Заполните поля";
                break;
        }
        msgView.setText(msg);
    }

}