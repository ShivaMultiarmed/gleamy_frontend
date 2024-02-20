package mikhail.shell.gleamy.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import mikhail.shell.gleamy.api.UserApi;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepo extends  AbstractRepo{
    private final UserApi userApi;
    public UserRepo()
    {
        super();
        userApi = webClient.createApi(UserApi.class);
    }
    public void getUsersByLogin(MutableLiveData<Map<Long, User>> usersData, String login)
    {
        Call<Map<Long, User>> request = userApi.getUsersByLogin(login);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<Long, User>> call, Response<Map<Long, User>> response) {
                        switch (response.code())
                        {
                            case 200 -> usersData.postValue(response.body());
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<Long, User>> call, Throwable t) {
                        usersData.postValue(null);
                    }
                }
        );
    }
    public void getUserById(PublishSubject<User> userObservable, long userid)
    {
        Call<User> request = userApi.getUserById(userid);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                switch(response.code())
                {
                    case 200 -> userObservable.onNext(response.body());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("UserRepo", "Error while fetching a user.");
            }
        });
    }
}
