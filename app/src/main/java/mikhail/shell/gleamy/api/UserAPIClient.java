package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mikhail.shell.gleamy.activities.CreateChatActivity;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserAPIClient extends AbstractAPI{
    private static UserAPIClient client;
    private UserApi userApi;
    private List<UserInfo> uinfos;


    private UserAPIClient()
    {
        userApi = getHttpClient().retrofit.create(UserApi.class);
    }
    public static UserAPIClient getClient()
    {
        if (client==null)
            client = new UserAPIClient();
        return client;
    }

    public void getUsersByLogin(String login)
    {
        CreateChatActivity curActivity = (CreateChatActivity) activities.get("CreateChatActivity");
        Call<Map<Long, UserInfo>> call = userApi.getUsersByLogin(login);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<Long, UserInfo>> call, Response<Map<Long, UserInfo>> response) {
                Map<Long, UserInfo> userInfos = response.body();
                curActivity.createUsers(userInfos);
                curActivity.displayUsers();
                curActivity.setChooseListener();
            }

            @Override
            public void onFailure(Call<Map<Long, UserInfo>> call, Throwable t) {

            }
        });
    }
}
