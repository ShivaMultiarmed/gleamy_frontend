package mikhail.shell.gleamy.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends AuthViewModel{
    private final static String TAG = "SignupViewModel";
    private final MutableLiveData<String> signupData;
    public SignupViewModel()
    {
        super();
        signupData = new MutableLiveData<>();
    }
    public LiveData getSignupData()
    {
        return signupData;
    }
    public void signup(String login, String password, String email)
    {
        Call<Map<String, Object>> request = authApi.signup(login, password, email);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {

                String code = String.valueOf(response.body().get("code"));
                signupData.postValue(code);

                if (code.equals("OK"))
                {
                    long userid = Long.parseLong((response.body().get("userid").toString()));
                    webClient.setUserStompConnection(userid);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e(TAG, "Error while signing up.");
            }
        });
    }
}
