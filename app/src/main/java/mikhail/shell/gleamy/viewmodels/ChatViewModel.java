package mikhail.shell.gleamy.viewmodels;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.api.MsgApi;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.UserInfo;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatViewModel extends ViewModel {
    private final static String TAG = ChatViewModel.class.getName();
    private MutableLiveData<ChatInfo> chatData;
    private MutableLiveData<Map<Long,MsgInfo>> msgListData;
    private MessagesRepo msgsRepo;
    private ChatsRepo chatsRepo;
    public ChatViewModel(ChatInfo chat)
    {
        initLiveData(chat);
        initRepos();
        initSubConsumer(chat.getId());
    }
    private void initRepos()
    {
        msgsRepo = new MessagesRepo();
        chatsRepo = new ChatsRepo();
    }
    private void initLiveData(ChatInfo chat)
    {
        chatData = new MutableLiveData<>();
        chatData.setValue(chat);
        msgListData = new MutableLiveData<>();
    }
    private void initSubConsumer(long chatid)
    {
        msgsRepo.observeIncomingMessage(msgListData, chatid);
    }
    public void fetchAllMessages()
    {
        long chatid = chatData.getValue().getId();
        msgsRepo.fetchAllMessages(msgListData, chatid);
    }
    public void sendMessage(String text) {

        MsgInfo msg = generateMessage(text);
        msgsRepo.sendMessage(msg);
    }
    private MsgInfo generateMessage(String text)
    {
        long ownUserId = GleamyApp.getApp().getUser().getId();
        long chatid = chatData.getValue().getId();
        MsgInfo msg = new MsgInfo(ownUserId,chatid,0,true,text);
        return  msg;
    }
    public MsgInfo getLastMessage()
    {
        Collection<MsgInfo> msgInfos = msgListData.getValue().values();
        MsgInfo lastMsg = null;
        for (Iterator<MsgInfo> iterator = msgInfos.iterator(); iterator.hasNext();)
            lastMsg = iterator.next();
        //List<MsgInfo> msgsList = msgListData.getValue().values().stream().collect(Collectors.toList());
        //MsgInfo lastMsg = msgsList.get(msgsList.size() - 1);
        return lastMsg;
    }
    public LocalDate getLastMsgDate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getLastMessage() != null && getLastMessage().getDateTime() != null) {
            return getLastMessage().getDateTime().toLocalDate();
        }
        else
            return null;
    }
    public void observeMessages(LifecycleOwner subscriber, Observer<Map<Long, MsgInfo>> observer)
    {
        msgListData.observe(subscriber, observer);
    }
    public void removeObservers(LifecycleOwner subscriber)
    {
        msgListData.removeObservers(subscriber);
    }
    public static class ChatViewModelFactory implements ViewModelProvider.Factory {
        private final ChatInfo chat;
        public ChatViewModelFactory(ChatInfo chat)
        {
            this.chat = chat;
        }
        @Override
        public <T extends ViewModel> T create(Class<T> type)
        {
            return (T) new ChatViewModel(chat);
        }
    }
    public void fetchAllChatMembers()
    {
        chatsRepo.fetchAllChatMembers(chatData, chatData.getValue().getId());
    }
}
