package mikhail.shell.gleamy.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsRepo extends AbstractRepo{
    private final ChatApi chatApi;
    private static ChatsRepo instance;
    private ChatsRepo(Context context)
    {
        super(context);
        chatApi = webClient.createApi(ChatApi.class);
    }
    public static ChatsRepo getInstance(Context context)
    {
        if (instance == null)
            instance = new ChatsRepo(context);
        return instance;
    }
    public void fetchAllChats(MutableLiveData<Map<Long,Chat>> chatsData, long userid)
    {
        Call<Map<Long, Chat>> request = chatApi.getAllChats(userid);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<Long, Chat>> call, Response<Map<Long, Chat>> response) {
                        switch(response.code())
                        {
                            case 200 -> {
                                Map<Long, Chat> chatMap  = response.body();
                                if(chatMap != null) {
                                    if (!chatMap.isEmpty()) {
                                        chatsData.postValue(new LinkedHashMap<>(chatMap));
                                        subscribeToChats(chatsData, chatMap.keySet());
                                    }
                                    else
                                        chatsData.postValue(new LinkedHashMap<>());
                                    subscribeToIncomingChats(chatsData);
                                }
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<Map<Long, Chat>> call, Throwable t) {
                        Log.e("ChatsRepo", "Error fetching all chats");
                    }
                }
        );
    }
    private void subscribeToChats(MutableLiveData<Map<Long,Chat>> chatsData, Set<Long> chatids)
    {
        chatids.stream().forEachOrdered(chatid -> subscribeToChat(chatsData, chatid));
    }
    private void subscribeToIncomingChats(MutableLiveData<Map<Long,Chat>> chatsData)
    {
        long userid = GleamyApp.getApp().getUser().getId();
        webClient.observeSubscription("/topic/users/" + userid + "/chats",
                incomingChat -> {
                    Chat chat = webClient.deserializePayload(incomingChat, Chat.class);
                    chatsData.getValue().put(chat.getId(), chat);
                    chatsData.postValue(chatsData.getValue());
                    subscribeToChat(chatsData, chat.getId());
                }
        );
    }
    private void subscribeToChat(MutableLiveData<Map<Long, Chat>> chatsData, long chatid)
    {
        webClient.observeSubscription(
                "/topic/chats/"+ chatid,
                incomingMessage -> {
                    Message msg = webClient.deserializePayload(incomingMessage, Message.class);
                    Map<Long, Chat> chats = chatsData.getValue();
                    Chat chat = chats.get(chatid);
                    chat.setLast(msg);
                    chats.remove(chat.getId());
                    chats.put(chatid, chat);

                    chatsData.postValue(chats);
                }
        );
    }
    public void fetchAllChatMembers(MutableLiveData<Chat> chatData, long chatid)
    {
        Call<List<User>> request = chatApi.getChatMembers(chatid);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        /*Map<Long, User> userMap =
                                response.body().stream().collect(Collectors.toMap(user -> user.getId(),user -> user));
                        Chat chat = chatData.getValue();
                        chat.setUsers(userMap);
                        chatData.postValue(chat);*/
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Log.e("ChatViewModel","Error fetching members from REST");
                    }
                }
        );
    }
    public void postChat(MutableLiveData<String> statusData, Chat chat)
    {
        Call<Map<String, String>> request = chatApi.addChat(chat);
        request.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                        statusData.postValue("OK");
                    }

                    @Override
                    public void onFailure(Call<Map<String, String>> call, Throwable t) {
                        statusData.postValue("ERROR");
                    }
                }
        );
    }
    public void logout()
    {
        webClient.disconnectFromStomp();
    }
}
