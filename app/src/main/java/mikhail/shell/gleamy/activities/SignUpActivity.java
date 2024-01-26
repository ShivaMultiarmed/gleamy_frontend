package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.AuthAPIClient;
import mikhail.shell.gleamy.databinding.SignUpActivityBinding;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {
    private SignUpActivityBinding B;
    private UserViewModel userViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = SignUpActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initViews();
    }
    private void initViews()
    {
        B.signUpBtn.setOnClickListener(e->{
            String code = validateLocal();
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                userViewModel.signup(
                        B.signUpName.getText().toString(),
                        B.signUpPassword.getText().toString(),
                        B.signUpEmail.getText().toString()
                );
        });
    }
    private String validateLocal()
    {
        if (B.signUpName.getText().toString().equals("") || B.signUpPassword.getText().toString().equals("") ||
                B.signUpEmail.getText().toString().equals(""))
            return "EMPTY";
        else
            return "LOCALOK";
    }
    public void displayMessage(String code)
    {
        String msg = switch (code) {
            case "OK" -> "Вы успешно зарегистрировались.";
            case "EMPTY" -> "Заполните поля.";
            default -> "Произошла ошибка.";
        };
        B.signUpMsg.setText(msg);
    }
}