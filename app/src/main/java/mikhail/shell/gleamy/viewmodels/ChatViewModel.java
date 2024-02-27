package mikhail.shell.gleamy.viewmodels;

import android.app.Notification;
import android.content.Context;
import android.os.Build;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;

public class ChatViewModel extends ViewModel {
    private final static String TAG = ChatViewModel.class.getName();
    private MutableLiveData<Chat> chatData;
    private MutableLiveData<ActionModel<Message>> msgsData;
    private MutableLiveData<ActionModel<User>> memberData;
    private MutableLiveData<ActionModel<byte[]>> avatarData;
    private MutableLiveData<Message> lastMsgData;
    private MessagesRepo msgsRepo;
    private ChatsRepo chatsRepo;
    public ChatViewModel(Chat chat)
    {
        initLiveData(chat);
        initRepos();
    }
    private void initRepos()
    {
        Context appContext = GleamyApp.getApp().getApplicationContext();
        msgsRepo = MessagesRepo.getInstance(appContext);
        chatsRepo = ChatsRepo.getInstance(appContext);
    }
    private void initLiveData(Chat chat)
    {
        chatData = new MutableLiveData<>();
        chatData.setValue(chat);
        msgsData = new MutableLiveData<>();
        memberData = new MutableLiveData<>();
        avatarData = new MutableLiveData<>();
    }
    public void observeIncomingMessage(LifecycleOwner owner, Observer<ActionModel<Message>> observer)
    {
        msgsData.observe(owner,observer);
        msgsRepo.observeIncomingMessage(msgsData, chatData.getValue().getId());
    }
    public void fetchAllMessages(LifecycleOwner subscriber, Observer<Map<Long, Message>> observer)
    {
        long chatid = chatData.getValue().getId();
        MutableLiveData<Map<Long, Message>> msgsData = new MutableLiveData<>();
        msgsData.observe(subscriber,observer);
        msgsRepo.fetchAllMessages(msgsData, chatid);
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
        return lastMsgData.getValue();
    }
    public LocalDate getLastMsgDate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getLastMessage() != null && getLastMessage().getDateTime() != null) {
            return getLastMessage().getDateTime().toLocalDate();
        }
        else
            return null;
    }
    public void removeObservers(LifecycleOwner subscriber)
    {
        msgsData.removeObservers(subscriber);
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
    public void fetchAllAvatarsByChatId(LifecycleOwner subscriber, Observer<ActionModel<byte[]>> observer)
    {
        avatarData.observe(subscriber, observer);
        List<Long> memberIds = chatData.getValue().getUsers().stream().map(User::getId).collect(Collectors.toList());
        chatsRepo.fetchMemberAvatars(avatarData, memberIds);
    }
    public LiveData<Chat> getChatData()
    {
        return chatData;
    }
}
