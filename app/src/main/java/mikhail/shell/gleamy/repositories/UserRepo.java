package mikhail.shell.gleamy.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.reactivex.subjects.PublishSubject;
import mikhail.shell.gleamy.api.UserApi;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public void getAvatarByUserId(MutableLiveData<byte[]> avatarData, Long userid)
    {
        Call<ActionModel<byte[]>> request = userApi.getUserAvatar(userid);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ActionModel<byte[]>> call, Response<ActionModel<byte[]>> response) {
                ActionModel<byte[]> actionModel = response.body();
                if (actionModel!=null)
                {
                    byte[] imageBytes = actionModel.getModel();
                    avatarData.postValue(imageBytes);
                }
                else
                    avatarData.postValue(null);
            }

            @Override
            public void onFailure(Call<ActionModel<byte[]>> call, Throwable t) {
                avatarData.postValue(null);
            }
        });
    }
    private MultipartBody.Part convertFile(String extension, byte[] fileContent)
    {
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("image/*"), fileContent);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("avatar", "avatar." + extension, requestFile);
        return body;
    }
    public void editAvatarByUserId(MutableLiveData<String> avatarData, Long userid, String extension, byte[] fileContent)
    {
        Call<Map<String, Object>> request = userApi.editAvatarByUserId(userid, convertFile(extension, fileContent));
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                String value =
                    switch (response.code())
                    {
                        case 200 -> (String) response.body().getOrDefault("filename", null);
                        case 404 -> "USERNOTFOUND";
                        case 400 -> "FILEERROR";
                        case 500 -> "UPLOADERROR";
                        default -> "ERROR";
                    };
                avatarData.postValue(value);
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                avatarData.postValue("ERROR");
            }
        });
    }
}
