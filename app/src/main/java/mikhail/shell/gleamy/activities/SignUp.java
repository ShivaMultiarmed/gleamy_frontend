package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.AuthAPIClient;
import retrofit2.http.Query;
//import java.net.http.HttpClient;

public class SignUp extends AppCompatActivity {

    private AuthAPIClient authAPIClient;
    private EditText userName, password, email;
    private TextView msgView;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
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
        AbstractAPI.addActivity("SignUp", this);
    }
    private void initViews()
    {
        userName = findViewById(R.id.signUpName);
        password = findViewById(R.id.signUpPassword);
        email = findViewById(R.id.signUpEmail);
        btn  = findViewById(R.id.signUpBtn);
        btn.setOnClickListener(e->{
            String code = validateLocal();
            if (!code.equals("LOCALOK"))
                displayMessage(code);
            else
                authAPIClient.signup(userName.getText().toString(), password.getText().toString(), email.getText().toString());
        });
        msgView = findViewById(R.id.signUpMsg);
    }
    private String validateLocal()
    {
        if (userName.getText().toString().equals("") || password.getText().toString().equals("") ||
                email.getText().toString().equals(""))
            return "EMPTY";
        else
            return "LOCALOK";
                    //authAPIClient.signup(userName.getText().toString(), password.getText().toString(), email.getText().toString());
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
        msgView.setText(msg);
    }
}