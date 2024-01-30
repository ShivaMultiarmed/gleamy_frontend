package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

//import java.net.http.HttpClient;

import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.databinding.LogInActivityBinding;
import mikhail.shell.gleamy.viewmodels.LoginViewModel;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class LogInActivity extends AppCompatActivity {
    private LogInActivityBinding B;
    private WebClient webClient;
    private LoginViewModel viewModel;

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
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.getLoginData().observe(this,
                (status) ->  {
                    displayMessage(status);
                    if (status.equals("OK"))
                        startChatsListActivity();
                }
        );
    }
    private void initBtn()
    {
        B.logInBtn.setOnClickListener(e->{
            String login = B.logInName.getText().toString(), password = B.logInPassword.getText().toString();
            String code = viewModel.validate(login,password);
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                viewModel.login(login, password);
        });
    }
    private void initLinkToSignUp()
    {
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
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