package mikhail.shell.gleamy.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import mikhail.shell.gleamy.api.AuthApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.UserInfo;

public abstract class AuthViewModel extends ViewModel {
    protected final WebClient webClient;
    protected final AuthApi authApi;
    public AuthViewModel()
    {
        webClient = WebClient.getInstance();
        authApi = webClient.createRetrofit(AuthApi.class);
    }
}
