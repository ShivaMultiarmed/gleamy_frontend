package mikhail.shell.gleamy.viewmodels;

import android.os.Build;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;

public class ChatViewModel extends ViewModel {
    private final static String TAG = ChatViewModel.class.getName();
    private MutableLiveData<Chat> chatData;
    private MutableLiveData<Map<Long, Message>> msgListData;
    private MessagesRepo msgsRepo;
    private ChatsRepo chatsRepo;
    public ChatViewModel(Chat chat)
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
    private void initLiveData(Chat chat)
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

        Message msg = generateMessage(text);
        msgsRepo.sendMessage(msg);
    }
    private Message generateMessage(String text)
    {
        long ownUserId = GleamyApp.getApp().getUser().getId();
        long chatid = chatData.getValue().getId();
        Message msg = new Message(ownUserId,chatid,0,true,text);
        return  msg;
    }
    public Message getLastMessage()
    {
        Collection<Message> messages = msgListData.getValue().values();
        Message lastMsg = null;
        for (Iterator<Message> iterator = messages.iterator(); iterator.hasNext();)
            lastMsg = iterator.next();
        //List<Message> msgsList = msgListData.getValue().values().stream().collect(Collectors.toList());
        //Message lastMsg = msgsList.get(msgsList.size() - 1);
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
    public void observeMessages(LifecycleOwner subscriber, Observer<Map<Long, Message>> observer)
    {
        msgListData.observe(subscriber, observer);
    }
    public void removeObservers(LifecycleOwner subscriber)
    {
        msgListData.removeObservers(subscriber);
    }
    public static class ChatViewModelFactory implements ViewModelProvider.Factory {
        private final Chat chat;
        public ChatViewModelFactory(Chat chat)
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
