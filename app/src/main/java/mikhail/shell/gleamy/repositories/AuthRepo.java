package mikhail.shell.gleamy.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.AuthApi;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthRepo extends AbstractRepo {
    private AuthApi authApi;
    private static AuthRepo instance;
    private AuthRepo(Context context) {
        super(context);
        authApi = webClient.createApi(AuthApi.class);
    }
    public static AuthRepo getInstance(Context context)
    {
        if (instance == null)
            instance = new AuthRepo(context);
        return instance;
    }
    public void signUp(MutableLiveData<String> signupData, String login, String password, String email) {
        Call<User> request = authApi.signup(new User(login, password, email));
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                String code =
                    switch (response.code())
                    {
                        case 200 -> "OK";
                        case 400 -> "USEREXISTS";
                        default -> "ERROR";
                    };
                signupData.postValue(code);
                if (code.equals("OK")) {
                    User user = response.body();
                    GleamyApp.getApp().setUser(user);
                    webClient.connectToStomp();
                    webClient.setUserStompConnection(user.getId());
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("AuthRepo", "Error while signing up.");
            }
        });
    }
}
