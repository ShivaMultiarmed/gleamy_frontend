package mikhail.shell.gleamy.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.CompletableTransformer;
import lombok.Getter;
import mikhail.shell.gleamy.activities.ChatInfoActivity;
import mikhail.shell.gleamy.activities.ChatsList;
import mikhail.shell.gleamy.activities.CreateChatActivity;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Getter
public class ChatAPIClient extends AbstractAPI {
    private long userid;
    private static  ChatAPIClient client;
    private ChatApi chatApi;
    private Map<Long,ChatInfo> chats;
    private ChatAPIClient()
    {
        chatApi = getHttpClient().retrofit.create(ChatApi.class);
    }

    public static ChatAPIClient getClient()
    {
        if (client == null)
            client = new ChatAPIClient();
        return client;
    }

    /*private void subscribe(long chatid)
    {
        String topic = "/topics/chats/"+chatid;
        getHttpClient().getSocket().send("{\"subscribe\": \""+topic+"\"}");
    }
    private void subscribeToAll(List<ChatInfo> chats)
    {
        for (ChatInfo chat : chats)
            subscribe(chat.getId());
    }*/
    public void getChatMembers(long chatid)
    {
        ChatInfoActivity chatInfo = (ChatInfoActivity) activities.get("ChatInfoActivity");
        Call<List<UserInfo>> call  = chatApi.getChatMembers(chatid);
        call.enqueue(new Callback<>()
            {
                @Override
                public void onResponse(Call<List<UserInfo>> call,  Response<List<UserInfo>> response)
                {
                    List<UserInfo> users = response.body();
                    chatInfo.addAllUsers(users);
                }
                @Override
                public void onFailure(Call<List<UserInfo>> call, Throwable t)
                {

                }
            }
        );
    }
    public void getAllChats(long userid)
    {
        ChatsList chatsList = (ChatsList)activities.get("ChatsList");
        Call<Map<Long, ChatInfo>> call = chatApi.getAllChats(userid);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<Long, ChatInfo>> call, Response<Map<Long, ChatInfo>> response) {
                chats = response.body();
                //subscribeToAll(new ArrayList<>(chats.values()));
                chatsList.displayAllChats(chats);
            }

            @Override
            public void onFailure(Call<Map<Long, ChatInfo>> call, Throwable t) {
                chats = null;
            }
        });
    }
    public void addChat(ChatInfo chatInfo)
    {
        CreateChatActivity createChatActivity = (CreateChatActivity)activities.get("CreateChatActivity");
        Call<Map<String, String>> call = chatApi.addChat(chatInfo);
        call.enqueue(new Callback<>(){
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response)
            {
                long chatid = Long.parseLong(response.body().get("chatid"));
                chatInfo.setId(chatid);

                for (Map.Entry<Long, UserInfo> entry : chatInfo.getUsers().entrySet())
                {
                    long curUser = entry.getKey();
                    notifyNewMember(curUser, chatInfo);
                }

                Intent backToChats = new Intent();
                Bundle b = new Bundle();
                b.putSerializable("chatinfo", (Serializable) chatInfo);
                backToChats.putExtras(b);


                createChatActivity.setResult(Activity.RESULT_OK, backToChats);
                createChatActivity.finish();
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t){

            }
        });
    }
    public void notifyNewMember(long userid, ChatInfo chatInfo)
    {
        //StompWrapper stompWrapper = new StompWrapper("NEWCHAT", chatInfo);
        String stomp = getHttpClient().getGson().toJson(chatInfo, Serializable.class);
        getHttpClient().getStompClient().send("/topic/users/"+userid, stomp).subscribe();
    }

}
