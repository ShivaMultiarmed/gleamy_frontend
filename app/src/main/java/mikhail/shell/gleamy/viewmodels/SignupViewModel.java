package mikhail.shell.gleamy.viewmodels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.repositories.AuthRepository;

public class SignupViewModel extends AuthViewModel{
    private final static String TAG = "SignupViewModel";
    private final MutableLiveData<String> signupData;
    private final AuthRepository authRepo;
    public SignupViewModel()
    {
        super();
        signupData = new MutableLiveData<>();
        Context context = GleamyApp.getApp().getApplicationContext();
        authRepo = AuthRepository.getInstance(context);
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
