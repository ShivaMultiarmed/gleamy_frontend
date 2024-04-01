package mikhail.shell.gleamy.viewmodels;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.UserRepository;

public class TheUserViewModel extends ViewModel {
    private final PublishSubject<User> userObservable;
    private final UserRepository userRepo;
    private final long userid;
    public TheUserViewModel(long userid)
    {
        userObservable = PublishSubject.create();
        this.userid = userid;

        Context appContext = GleamyApp.getApp().getApplicationContext();
        userRepo = UserRepository.getInstance(appContext);
    }
    public void getUser(Consumer<User> observer)
    {
        userObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        userRepo.getUserById(userObservable, userid);
    }
    public void getAvatar(LifecycleOwner subscriber, Observer<byte[]> observer)
    {
        MutableLiveData<byte[]> avatarData = new MutableLiveData<>();
        userRepo.getAvatarByUserId(avatarData, userid);
        avatarData.observe(subscriber, observer);
    }
    public void editAvatar(LifecycleOwner subscriber, String extension, byte[] content, Observer<String> observer)
    {
        MutableLiveData<String> avatarData = new MutableLiveData<>();
        userRepo.editAvatarByUserId(avatarData, userid, extension, content);
        avatarData.observe(subscriber, observer);
    }
    public void removeAvatar(LifecycleOwner subscriber, Long userid, Observer<Boolean> observer)
    {
        MutableLiveData<Boolean> avatarData = new MutableLiveData<>();
        userRepo.removeAvatarByUserId(avatarData, userid);
        avatarData.observe(subscriber, observer);
    }
    public static class UserViewModelFactory implements ViewModelProvider.Factory
    {
        private final long userid;
        public UserViewModelFactory(long userid)
        {
            this.userid = userid;
        }
        @Override
        public <T extends ViewModel> T create(Class<T> type) { return (T) new TheUserViewModel(userid);}
    }

}
