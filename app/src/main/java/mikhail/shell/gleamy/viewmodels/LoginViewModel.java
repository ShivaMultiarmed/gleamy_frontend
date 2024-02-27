package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AuthViewModel {
    private final MutableLiveData<String> loginData;

    public LoginViewModel()
    {
        super();
        loginData = new MutableLiveData<>();
    }
    public LiveData<String> getLoginData()
    {
        return loginData;
    }

    public void login(String login, String password)
    {
        Call<User> call = authApi.login(login, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String code;
                switch (response.code())
                {
                    case 200 -> {
                        User user = (User) response.body();
                        webClient.connectToStomp();
                        GleamyApp.getApp().setUser(user);
                        webClient.setUserStompConnection(user.getId());
                        code = "OK";
                    }
                    case 404 -> code = "NOTFOUND";
                    case 400 -> code = "PASSINCORRECT";
                    default -> code = "ERROR";
                }

                loginData.postValue(code);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                loginData.postValue("ERROR");
            }
        });
    }
    public String validate(String login, String password)
    {
        if (login.isEmpty() || password.isEmpty())
            return "EMPTY";
        else
            return "LOCALOK";
    }
}
