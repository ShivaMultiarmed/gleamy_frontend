package mikhail.shell.gleamy.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Getter;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;

@Getter
public class ChatAPIClient extends AbstractAPI {
    private long userid;
    private static  ChatAPIClient client;
    private ChatApi chatApi;
    private List<ChatInfo> chats;
    private ChatAPIClient()
    {
        chatApi = httpClient.retrofit.create(ChatApi.class);
    }

    public static ChatAPIClient getClient()
    {
        if (client == null)
            client = new ChatAPIClient();
        return client;
    }
    public List<ChatInfo> getAllChats(long userid)
    {/*
        Call<List<Chat>> call = chatApi.getAllChats(userid);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Chat>> call, Response<List<Chat>> response) {
                chats = response.body();
            }

            @Override
            public void onFailure(Call<List<Chat>> call, Throwable t) {

            }
        });*/
        chats = new ArrayList<>();
        for (int i = 1; i<=10; i++)
            chats.add(new ChatInfo(userid,
                    "Chat number " + i,
                    new MsgInfo(this.userid,1000, true, "Some text", new Date())));
        return chats;
    }
}
