package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.databinding.LogInActivityBinding;
import mikhail.shell.gleamy.models.UserInfo;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class LogInActivity extends AppCompatActivity {
    private LogInActivityBinding B;
    private WebClient webClient;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = LogInActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        webClient = WebClient.getInstance();
        initUserViewModel();
        initBtn();
        initLinkToSignUp();
    }
    private void initUserViewModel()
    {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        userViewModel.getResultCodeData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                displayMessage(status);
                if (status.equals("OK"))
                    startChatsListActivity();
            }
        });
    }
    private void initBtn()
    {
        B.logInBtn.setOnClickListener(e->{
            String login = B.logInName.getText().toString(), password = B.logInPassword.getText().toString();
            String code = userViewModel.validate(login,password);
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                userViewModel.login(login, password);
        });
    }
    private void initLinkToSignUp()
    {
        Intent signUpIntent = new Intent(this, SignUpActivity.class );
        B.linkToSignUp.setOnClickListener((view) -> startActivity(signUpIntent));
    }
    public void displayMessage(String status)
    {
        String msg =
            switch (status)
            {
                case "OK" -> "Вы успешно вошли";
                case "PASSINCORRECT" -> "Неверный пароль";
                case "NOTFOUND" -> "Пользователь не найден";
                case "EMPTY" -> "Заполните поля";
                default -> "Ошибка";
            };
        B.logInMessage.setText(msg);
    }
    private void startChatsListActivity()
    {
        Intent intent = new Intent(this, ChatsListActivity.class);
        startActivity(intent);
    }

}