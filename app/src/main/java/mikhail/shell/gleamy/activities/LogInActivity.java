package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;
//import java.net.http.HttpClient;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.AppHttpClient;
import mikhail.shell.gleamy.api.AuthAPIClient;
import mikhail.shell.gleamy.databinding.LogInActivityBinding;
import mikhail.shell.gleamy.models.UserInfo;
public class LogInActivity extends AppCompatActivity {
    private LogInActivityBinding B;
    private AppHttpClient httpClient;
    private AuthAPIClient authAPIClient;
    private List<UserInfo> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = LogInActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        initHttp();
        init();
        initBtn();
        initLinkToSignUp();
    }
    private void initHttp()
    {
        httpClient = AppHttpClient.getClient();
        authAPIClient = AuthAPIClient.getClient();
        AbstractAPI.addActivity("LogInActivity", this);
    }
    private void init()
    {

    }
    private void initBtn()
    {
        B.logInBtn.setOnClickListener(e->{
            String code = validate(B.logInName.getText().toString(), B.logInPassword.getText().toString());
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                authAPIClient.login(B.logInName.getText().toString(), B.logInPassword.getText().toString());
        });
    }
    private void initLinkToSignUp()
    {
        LogInActivity thisActivity = this;
        B.linkToSignUp.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(thisActivity, SignUpActivity.class ));
            }
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
        B.logInMessage.setText(msg);
    }

}