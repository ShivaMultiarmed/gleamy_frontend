package mikhail.shell.gleamy.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.subjects.PublishSubject;
import mikhail.shell.gleamy.api.UserApi;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepo extends  AbstractRepo{
    private final UserApi userApi;
    private static UserRepo instance;
    private UserRepo(Context context)
    {
        super(context);
        userApi = webClient.createApi(UserApi.class);
    }
    public static UserRepo getInstance(Context context)
    {
        if (instance == null)
            instance = new UserRepo(context);
        return instance;
    }
    public void getUsersByLogin(MutableLiveData<Map<Long, User>> usersData, String login)
    {
        Call<List<User>> request = userApi.getUsersByLogin(login);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        switch (response.code())
                        {
                            case 200 -> {
                                List<User> userList = response.body();
                                Map<Long, User> userMap = userList.stream()
                                        .collect(Collectors.toMap(User::getId, Function.identity()));
                                usersData.postValue(userMap);
                            }
                            default -> usersData.postValue(null);
                        }
                    }
                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
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
