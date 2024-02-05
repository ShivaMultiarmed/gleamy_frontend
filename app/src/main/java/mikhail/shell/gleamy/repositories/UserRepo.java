package mikhail.shell.gleamy.repositories;

import androidx.lifecycle.MutableLiveData;

import java.util.Map;

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
        userApi = webClient.createRetrofit(UserApi.class);
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
}
