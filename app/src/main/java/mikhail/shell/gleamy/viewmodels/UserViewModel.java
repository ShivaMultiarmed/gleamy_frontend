package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import mikhail.shell.gleamy.api.AuthApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.User;

public class UserViewModel extends ViewModel {
    private static final String TAG = "USER_VIEW_MODEL";
    private final MutableLiveData<User> userData;
    private final MutableLiveData<String> loginData, signupData;
    private final WebClient webClient;
    private final AuthApi authApi;

    public UserViewModel() {
        webClient = WebClient.getInstance();
        authApi = webClient.createApi(AuthApi.class);

        userData = new MutableLiveData<>();
        loginData = new MutableLiveData<>();
        signupData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserData() {
        return userData;
    }

}
