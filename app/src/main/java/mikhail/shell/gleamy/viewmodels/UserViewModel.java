package mikhail.shell.gleamy.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.AuthApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private static final String TAG = "USER_VIEW_MODEL";
    private final MutableLiveData<UserInfo> userData;
    private final MutableLiveData<String> resultCodeData;
    private final WebClient webClient;
    private final AuthApi authApi;
    public UserViewModel()
    {
        webClient = WebClient.getInstance();
        authApi = webClient.createRetrofit(AuthApi.class);

        userData = new MutableLiveData<>();
        resultCodeData = new MutableLiveData<>();
    }
    public MutableLiveData<UserInfo> getUserData()
    {
        return  userData;
    }
    public MutableLiveData<String> getResultCodeData()
    {
        return resultCodeData;
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
                if (code.equals("OK"))
                {
                    webClient.connect();
                    long userid = Long.valueOf(responseDetails.get("userid"));
                    webClient.setUserStompConnection(userid);
                    UserInfo user = new UserInfo(userid, login, password); // Correct this line
                    userData.postValue(user);
                    GleamyApp.getApp().setUser(user);
                }
                resultCodeData.postValue(code);
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.e(TAG, "Error while logging on the server.");
            }
        });
    }
    public String validate(String login, String password)
    {
        if (login.equals("") || password.equals(""))
            return "EMPTY";
        else
            return "LOCALOK";
    }
}
