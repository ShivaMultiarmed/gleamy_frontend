package mikhail.shell.gleamy.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import java.util.Map;

import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;

public class MessagesService extends Service {
    private MessagesRepo messagesRepo;
    private ChatsRepo chatsRepo;
    private MutableLiveData<ActionModel<Message>> msgData;
    @Override
    public void onCreate() {
        super.onCreate();
        messagesRepo = new MessagesRepo(); // make a single instance !
        chatsRepo = new ChatsRepo(); // make a single instance !
        msgData = new MutableLiveData<>();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void observeIncomingMessages()
    {
        messagesRepo.observeIncomingMessage(msgData, 1);
    }
}
