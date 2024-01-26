package mikhail.shell.gleamy.viewmodels;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.MsgApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
    private final static String TAG = "ChatViewModel";
    private MutableLiveData<ChatInfo> chatData;
    private MutableLiveData<Map<Long,MsgInfo>> msgListData;
    private MutableLiveData<MsgInfo> lastMsgData;
    private WebClient webClient;
    private MsgApi msgApi;
    public ChatViewModel(ChatInfo chat)
    {
        webClient = WebClient.getInstance();

        initLiveData();
        initRetrofits();
    }
    private void initLiveData()
    {
        chatData = new MutableLiveData<>();
        msgListData = new MutableLiveData<>();
        lastMsgData = new MutableLiveData<>();
    }
    private void initRetrofits()
    {
        msgApi = webClient.createRetrofit(MsgApi.class);
    }
    public void fetchAllMessagesFromREST()
    {
        long chatid = chatData.getValue().getId();
        Call<List<MsgInfo>> call = msgApi.getChatMsgs(chatid);
        call.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<MsgInfo>> call, Response<List<MsgInfo>> response) {
                        List<MsgInfo> msgsList = response.body();
                        Map<Long, MsgInfo> msgMap = msgsList.stream().collect(Collectors.toMap(msg -> msg.msgid,msg -> msg));
                        msgListData.postValue(msgMap);
                    }

                    @Override
                    public void onFailure(Call<List<MsgInfo>> call, Throwable t) {
                        msgListData.postValue(null); // handle error via toast in activity
                    }
                }
        );
    }
    public void sendMessage(String text)
    {
        ChatInfo chat = chatData.getValue();
        Set<Long> userids = chat.getUsers().keySet();
        long ownUserId = GleamyApp.getApp().getUser().getId();
        long chatid = chatData.getValue().getId();
        MsgInfo msg = new MsgInfo(ownUserId,chatid,0,true,text);
        for (long userid : userids)
            webClient.sendStompMessage("/topics/users/" + userid, msg);
    }
    public MsgInfo getLastMessage()
    {
        return lastMsgData.getValue();
    }
    public LocalDate getLastMsgDate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return getLastMessage().getDateTime().toLocalDate();
        }
        else
            return null;
    }
    public void observeAllMessages(LifecycleOwner subscriber, Observer<Map<Long, MsgInfo>> observer)
    {
        msgListData.observe(subscriber, observer);
    }
    public void observeReceivedMessage(LifecycleOwner subscriber, Observer<MsgInfo> observer)
    {
        lastMsgData.observe(subscriber, observer);
    }
}
