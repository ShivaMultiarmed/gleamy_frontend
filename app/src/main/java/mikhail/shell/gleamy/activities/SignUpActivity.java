package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import mikhail.shell.gleamy.databinding.SignUpActivityBinding;
import mikhail.shell.gleamy.viewmodels.SignupViewModel;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {
    private SignUpActivityBinding B;
    private SignupViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = SignUpActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        viewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        initBtn();
        viewModel.observeSignUpStatus(this,
                statusCode -> {
                    displayMessage(statusCode);
                    if (statusCode.equals("OK"))
                        goToChats();
                }
        );
    }

    private void initBtn()
    {
        B.signUpBtn.setOnClickListener(e->{
            String name = B.signUpName.getText().toString(),
            password = B.signUpPassword.getText().toString(),
            email = B.signUpEmail.getText().toString();

            viewModel.tryToSignUp(name, password, email);
        });
    }
    private void displayMessage(String code)
    {
        String msg = switch (code) {
            case "OK" -> "Вы успешно зарегистрировались.";
            case "EMPTY" -> "Заполните поля.";
            default -> "Произошла ошибка.";
        };
        B.signUpMsg.setText(msg);
    }
    private void goToChats()
    {
        Intent openChats = new Intent(this, ChatsListActivity.class);
        startActivity(openChats);
        finish();
    }

}