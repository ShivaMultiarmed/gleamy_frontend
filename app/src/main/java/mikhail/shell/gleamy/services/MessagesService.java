package mikhail.shell.gleamy.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.repositories.ChatsRepo;
import mikhail.shell.gleamy.repositories.MessagesRepo;

public class MessagesService extends Service {
    private final static String CHANNEL_ID = "messagesChannel", TAG = "MessageService";
    private static int NOTIFICATION_ID = 0;
    private MessagesRepo messagesRepo;
    private ChatsRepo chatsRepo;
    private MutableLiveData<ActionModel<Message>> msgData;
    private Observer<ActionModel<Message>> msgObserver;
    private NotificationManager notificationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        messagesRepo = new MessagesRepo(); // make a single instance !
        chatsRepo = new ChatsRepo(); // make a single instance !
        msgData = new MutableLiveData<>();
        initMessageObservers();
        observeIncomingMessages();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        initPushNotificationsChannel();
        Log.i(TAG, "message service is created.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initMessageObservers() {
        MutableLiveData<Map<Long, Chat>> chatData = new MutableLiveData<>();
        long userid = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
        chatsRepo.fetchAllChats(chatData, userid);
        Observer<Map<Long, Chat>> observer = new Observer<>() {
            @Override
            public void onChanged(Map<Long, Chat> chatMap) {
                chatMap.keySet().forEach(
                        chatid -> {
                            messagesRepo.observeIncomingMessage(msgData, chatid);
                            chatData.removeObserver(this);
                        }
                );
            }
        };
        chatData.observeForever(observer);
    }

    private void observeIncomingMessages() {
        msgObserver = messageActionModel -> {
            Message msg = messageActionModel.getObject();
            displayPushNotification(msg);
        };
        msgData.observeForever(msgObserver);
    }

    private void initPushNotificationsChannel()
    {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"messagesChannel", NotificationManager.IMPORTANCE_DEFAULT);
        notificationManager.createNotificationChannel(channel);
    }

    private void displayPushNotification(Message msg)
    {
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.btn_bg)
                .setContentTitle("Gleamy")
                .setContentText(msg.getText())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
        NotificationManagerCompat.from(this).notify(++NOTIFICATION_ID, notification);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        msgData.removeObserver(msgObserver);
    }
}
