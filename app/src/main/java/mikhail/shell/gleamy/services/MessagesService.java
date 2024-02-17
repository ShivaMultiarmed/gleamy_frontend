package mikhail.shell.gleamy.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;

public class MessagesService extends Service {
    private MessagesRepo messagesRepo;
    private ChatsRepo chatsRepo;
    private MutableLiveData<Message> lastMessageData;
    @Override
    public void onCreate() {
        super.onCreate();
        messagesRepo = new MessagesRepo(); // make a single instance !
        chatsRepo = new ChatsRepo(); // make a single instance !
        lastMessageData = new MutableLiveData<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void observeIncomingMessages()
    {
        MutableLiveData<Map<Long, Chat>> chatsData = new MutableLiveData<>();
        //chatsRepo.fetchAllChats();
        messagesRepo.observeLastIncomingMessage(lastMessageData, 1);
    }
}
