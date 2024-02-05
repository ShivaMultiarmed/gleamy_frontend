package mikhail.shell.gleamy.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

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
    public ChatsRepo()
    {
        super();
        chatApi = webClient.createRetrofit(ChatApi.class);
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
                                        chatsData.postValue(chatMap);
                                        subscribeToIncomingChats(chatsData);
                                        subscribeToChats(chatsData, chatMap.keySet());
                                    }
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
        chatids.stream().forEachOrdered(chatid -> webClient.observeSubscription(
                "/topic/chats/"+ chatid,
                incomingMessage -> {
                    Message msg = webClient.deserializePayload(incomingMessage, Message.class);
                    Log.d("ChatsRepo", msg.getText());
                    Map<Long, Chat> chats = chatsData.getValue();
                    Chat chat = chats.get(chatid);
                    chat.setLast(msg);
                    chats.replace(chat.getId(), chat);

                    chatsData.postValue(chats);
                }
        ));
    }
    private void subscribeToIncomingChats(MutableLiveData<Map<Long,Chat>> chatsData)
    {
        long userid = GleamyApp.getApp().getUser().getId();
        webClient.observeSubscription("/topic/users/" + userid + "/chats",
                incomingChat -> {
                    Chat chat = webClient.deserializePayload(incomingChat, Chat.class);
                    chatsData.getValue().put(chat.getId(), chat);
                    chatsData.postValue(chatsData.getValue());
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
    public void sendChat(MutableLiveData<Map<Long, Chat>> chatsData, Chat chat)
    {
        Call<Map<String, String>> request = chatApi.addChat(chat);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                switch (response.code())
                {
                    case 200 -> {
//                        Map<String, String> chatDetails = response.body();
//                        long chatid = Long.parseLong(chatDetails.get("chatid"));
//                        chat.setId(chatid);
//                        chat.getUsers().keySet().stream().forEach(uid -> notifyNewChatMember(uid, chat));
                    }
                    default -> Log.d("Chats Repository", "Error adding a chat to the server.");
                }
            }
            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                Log.d("Chats Repository", "Error adding a chat to the server.");
            }
        });
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
}
