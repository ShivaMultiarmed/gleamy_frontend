package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import mikhail.shell.gleamy.activities.ChatsList;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class ChatAPIClient extends AbstractAPI {
    private long userid;
    private static  ChatAPIClient client;
    private ChatApi chatApi;
    private Map<Long,ChatInfo> chats;
    private Map<String, Activity> activities;
    private ChatAPIClient()
    {
        chatApi = httpClient.retrofit.create(ChatApi.class);
        activities = new HashMap<>();
    }

    public static ChatAPIClient getClient()
    {
        if (client == null)
            client = new ChatAPIClient();
        return client;
    }
    public void addActivity(String name, Activity activity)
    {
        activities.put(name, activity);
    }
    public void getAllChats(long userid)
    {
        ChatsList chatsList = (ChatsList)activities.get("ChatsList");
        Call<Map<Long, ChatInfo>> call = chatApi.getAllChats(userid);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<Long, ChatInfo>> call, Response<Map<Long, ChatInfo>> response) {
                chats = response.body();
                chatsList.displayAllChats(chats);
            }

            @Override
            public void onFailure(Call<Map<Long, ChatInfo>> call, Throwable t) {
                chats = null;
            }
        });
        /*
        chats = new ArrayList<>();
        for (int i = 1; i<=10; i++)
            chats.add(new ChatInfo(userid,
                    "Chat number " + i,
                    new MsgInfo(this.userid,1000, true, "Some text", new Date())));*/
    }
}
