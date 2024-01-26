package mikhail.shell.gleamy.viewmodels;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.activities.ChatsListActivity;
import mikhail.shell.gleamy.api.AuthApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserViewModel extends ViewModel {
    private static final String TAG = "USER_VIEW_MODEL";
    private final MutableLiveData<UserInfo> userData;
    private final MutableLiveData<String> loginData, signupData;
    private final WebClient webClient;
    private final AuthApi authApi;

    public UserViewModel() {
        webClient = WebClient.getInstance();
        authApi = webClient.createRetrofit(AuthApi.class);

        userData = new MutableLiveData<>();
        loginData = new MutableLiveData<>();
        signupData = new MutableLiveData<>();
    }

    public MutableLiveData<UserInfo> getUserData() {
        return userData;
    }

}
