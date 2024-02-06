package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

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
        Call<Map<String, String>> call = authApi.login(login, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> responseDetails = response.body();
                String code;
                if (response.isSuccessful())
                    code = responseDetails.get("code");
                else
                    code = "ERROR";
                if (code.equals("OK")) {
                    webClient.connectToStomp();
                    long userid = Long.parseLong(responseDetails.get("userid"));
                    GleamyApp.getApp().setUser(new User(userid,login,password));
                    webClient.setUserStompConnection(userid);
                }
                loginData.postValue(code);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
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
