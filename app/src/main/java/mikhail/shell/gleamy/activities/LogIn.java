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

    private EditText login, password;
    private Button btn;
    private TextView msgView;
    private List<UserInfo> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);
        initHttp();
        init();
        initBtn();
    }
    private void initHttp()
    {
        httpClient = AppHttpClient.getClient();
        authAPIClient = AuthAPIClient.getClient();
        authAPIClient.addActivity("LogIn", this);
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
            String code = validate(login.getText().toString(), password.getText().toString());
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                authAPIClient.login(login.getText().toString(), password.getText().toString());
        });
    }
    private String validate(String login, String password)
    {
        if (login.equals("") || password.equals(""))
            return "EMPTY";
        else
            return "LOCALOK";

    }

    private UserInfo getUser(String login)
    {
        UserInfo user = null;
        for (UserInfo u:users)
            if (Objects.equals(u.login, login))
                user = u;
        return user;
    }
    public void displayMessage(String status)
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