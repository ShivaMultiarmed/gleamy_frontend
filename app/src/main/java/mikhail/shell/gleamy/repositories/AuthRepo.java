package mikhail.shell.gleamy.repositories;

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
    public AuthRepo() {
        authApi = webClient.createApi(AuthApi.class);
    }
    public void signUp(MutableLiveData<String> signupData, String login, String password, String email) {
        Call<Map<String, Object>> request = authApi.signup(login, password, email);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                switch (response.code())
                {
                    case 200 ->{
                        String code = String.valueOf(response.body().get("code"));
                        if (code.equals("OK")) {
                            long userid = Long.parseLong((response.body().get("userid").toString()));
                            GleamyApp.getApp().setUser(new User(userid,login, password));
                            webClient.connectToStomp();
                            webClient.setUserStompConnection(userid);
                        }
                        signupData.postValue(code);
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("AuthRepo", "Error while signing up.");
            }
        });
    }
}
