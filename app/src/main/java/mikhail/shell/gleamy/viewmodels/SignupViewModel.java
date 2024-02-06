package mikhail.shell.gleamy.viewmodels;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Map;

import mikhail.shell.gleamy.repositories.AuthRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupViewModel extends AuthViewModel{
    private final static String TAG = "SignupViewModel";
    private final MutableLiveData<String> signupData;
    private final AuthRepo authRepo;
    public SignupViewModel()
    {
        super();
        signupData = new MutableLiveData<>();
        authRepo = new AuthRepo();
    }
    public void observeSignUpStatus(LifecycleOwner subscriber, Observer<String> statusObserver)
    {
        signupData.observe(subscriber, statusObserver);
    }
    public String validateLocal(String name, String password, String email)
    {
        if (name.isEmpty() || password.isEmpty() || email.isEmpty())
            return "EMPTY";
        else
            return "LOCALOK";
    }
    public void tryToSignUp(String name, String password, String email)
    {
        String local = validateLocal(name, password, email);
        if (!local.equals("LOCALOK"))
            signupData.postValue(local);
        else
            authRepo.signUp(signupData,name, password, email);
    }
}
