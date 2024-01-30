package mikhail.shell.gleamy.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsRepo extends AbstractRepo{
    private final ChatApi chatApi;
    public ChatsRepo()
    {
        super();
        chatApi = webClient.createRetrofit(ChatApi.class);
    }
    public void fetchAllChatMembers(MutableLiveData<ChatInfo> chatData, long chatid)
    {
        Call<List<UserInfo>> request = chatApi.getChatMembers(chatid);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<UserInfo>> call, Response<List<UserInfo>> response) {
                        /*Map<Long, UserInfo> userMap =
                                response.body().stream().collect(Collectors.toMap(user -> user.getId(),user -> user));
                        ChatInfo chat = chatData.getValue();
                        chat.setUsers(userMap);
                        chatData.postValue(chat);*/
                    }

                    @Override
                    public void onFailure(Call<List<UserInfo>> call, Throwable t) {
                        Log.e("ChatViewModel","Error fetching members from REST");
                    }
                }
        );
    }
}
