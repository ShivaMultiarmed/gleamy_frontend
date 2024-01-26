package mikhail.shell.gleamy.viewmodels;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsListViewModel extends ViewModel {
    private final static String TAG = "CHATS_VIEW_MODEL", TOPIC_USER_PREFIX = "/topics/users/";
    private final WebClient webClient;
    private final MutableLiveData<Map<Long, ChatInfo>> chatsLiveData;
    private final MutableLiveData<ChatInfo> latestChatLiveData;
    private ChatApi chatApi;
    public ChatsListViewModel()
    {
        webClient = WebClient.getInstance();
        chatsLiveData = new MutableLiveData<>();
        chatsLiveData.postValue(new LinkedHashMap<>());
        latestChatLiveData = new MutableLiveData<>();

        initRetrofits();
    }
    public MutableLiveData<Map<Long, ChatInfo>> getChatsLiveData()
    {
        return chatsLiveData;
    }
    public MutableLiveData<ChatInfo> getLatestChatLiveData() {   return latestChatLiveData;}
    public Map<Long, ChatInfo> getAllChats()
    {
        return chatsLiveData.getValue();
    }
    private void initRetrofits()
    {
        chatApi = webClient.createRetrofit(ChatApi.class);
    }
    public void fetchAllChatsFromREST()
    {
        final long userid = GleamyApp.getApp().getUser().getId();
        Call<Map<Long, ChatInfo>> call = chatApi.getAllChats(userid);
        call.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<Long, ChatInfo>> call, Response<Map<Long, ChatInfo>> response) {
                        Map<Long, ChatInfo> chats = response.body();
                        chatsLiveData.postValue(chats);
                        Collection<ChatInfo> chatCollection = chats.values();
//                        ChatInfo lastChat = chatCollection.stream().sorted(
//                                (chat1, chat2) -> (chat1.getId() - chat2.getId())
//                        ).findAny().get();
                        //latestChatLiveData.postValue(lastChat);
                    }
                    @Override
                    public void onFailure(Call<Map<Long, ChatInfo>> call, Throwable t) {
                        Log.d(TAG, "Error while fetching all chats from the server.");
                    }
                }
        );
    }
    public void addChat(ChatInfo chat)
    {
        Call<Map<String, String>> call = chatApi.addChat(chat);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                Map<String, String> chatDetails = response.body();
                long chatid = Long.valueOf(chatDetails.get("chatid"));

                Set<Map.Entry<Long, UserInfo>> users = chat.getUsers().entrySet();
                for (Map.Entry<Long, UserInfo> user : users)
                {
                    long userid = user.getKey();
                    notifyNewChatMember(userid, chat);
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.d(TAG, "Error adding a chat to the server.");
            }
        });
    }
    public void notifyNewChatMember(long userid, ChatInfo chat)
    {
        webClient.sendStompMessage(TOPIC_USER_PREFIX + userid, chat);
    }
    @Override
    protected void onCleared()
    {
        Log.i(TAG, "The viewmodel is cleared.");
    }

    public void setObserverToAllMessages(Observer<Map<Long, MsgInfo>> observer)
    {
        // to do
    }
}
