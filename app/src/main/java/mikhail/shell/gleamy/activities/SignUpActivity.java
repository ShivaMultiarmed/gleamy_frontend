package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.AuthAPIClient;
import mikhail.shell.gleamy.databinding.SignUpActivityBinding;

public class SignUpActivity extends AppCompatActivity {
    private SignUpActivityBinding B;
    private AuthAPIClient authAPIClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        B = SignUpActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initHttp();
        initViews();
    }
    private void initHttp()
    {
        authAPIClient = AuthAPIClient.getClient();
        AbstractAPI.addActivity("SignUpActivity", this);
    }
    private void initViews()
    {
        B.signUpBtn.setOnClickListener(e->{
            String code = validateLocal();
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                authAPIClient.signup(
                        B.signUpName.getText().toString(),
                        B.signUpPassword.getText().toString(),
                        B.signUpEmail.getText().toString());
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
        String msg;
        switch (code)
        {
            case "OK":
                msg = "Вы успешно зарегистрировались.";
                break;

            case "EMPTY":
                msg = "Заполните поля.";
                break;
            default:
                msg = "Произошла ошибка.";
                break;
        }
        B.signUpMsg.setText(msg);
    }
}