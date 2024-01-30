package mikhail.shell.gleamy.api;

import android.os.Build;
import android.util.Log;

import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mikhail.shell.gleamy.activities.ChatActivity;
import mikhail.shell.gleamy.api.json.adapters.DateTimeAdapter;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.LifecycleEvent;
import ua.naiksoftware.stomp.dto.StompMessage;

public class WebClient {
    private  final  static String TAG = "WebClient";
    private static WebClient webClient;
    private final static int PORT = 8080, HEARTBEAT = 1000;
    private final static String HOST = "158.160.22.54",
             CONTEXT_PATH = "/gleamy", WS_ENDPOINT = "/websocket";
    private enum StompType
    {
        RECEIVEDMESSAGE, NEWCHAT
    }
    private Gson gson;
    private OkHttpClient okHttpClient;
    private StompClient stompClient;
    private Retrofit retrofit;
    private SubscriptionsManager subscriptionsManager;
    private WebClient()
    {
        initGson();
        initOkHttpClient();
        initRetrofit();

        initStompClient();
        initSubscriptionsManager();
    }

    public static WebClient getInstance()
    {
        if (webClient == null)
            webClient = new WebClient();
        return webClient;
    }

    private void initOkHttpClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        okHttpClient = builder.build();
    }
    private void initStompClient()
    {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
                "ws://" + HOST + ":" + PORT + CONTEXT_PATH + WS_ENDPOINT);
    }
    private void initGson()
    {
        GsonBuilder builder = new GsonBuilder();
        //builder.registerTypeAdapter(Serializable.class, new AppHttpClient.InterfaceAdapter<Serializable>());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.registerTypeAdapter(LocalDateTime.class, new DateTimeAdapter());
        }
        gson = builder.create();
    }
    private void initSubscriptionsManager()
    {
        subscriptionsManager = SubscriptionsManager.getInstance();
    }
    private void initRetrofit()
    {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + HOST + ":" + PORT + CONTEXT_PATH + "/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }
    public void connect()
    {
        stompClient.connect();
        stompClient.withClientHeartbeat(HEARTBEAT).withClientHeartbeat(HEARTBEAT);
    }
    private void setStompLifeCycle(Consumer<LifecycleEvent> consumer)
    {
        stompClient.lifecycle().subscribe(consumer);
    }
    private Consumer<LifecycleEvent> createConsumer(long userid)
    {
        return lifecycleEvent -> {

            switch (lifecycleEvent.getType())
            {
                case OPENED -> onStompOpened(lifecycleEvent, userid);
                case CLOSED -> onStompClosed(lifecycleEvent);
                case ERROR -> onStompError(lifecycleEvent);
                case FAILED_SERVER_HEARTBEAT -> onFailedHearbeat(lifecycleEvent);
            }

        };
    }
    private void onStompOpened(LifecycleEvent e, long userid)
    {
        //subscribeToUserTopic(userid);
    }
    private void onStompClosed(LifecycleEvent e) {
        //Log.e(TAG, e.getMessage());
    }
    private void onStompError(LifecycleEvent e) {
        //Log.e(TAG, e.getMessage());
    }
    private void onFailedHearbeat(LifecycleEvent e) {
        Log.e(TAG, e.getMessage());
    }

    private void subscribeToUserTopic(long userid)
    {
        stompClient.topic("/topic/users/" + userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createMessageHandler());
    }

    private Consumer<StompMessage> createMessageHandler() {
        return new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage msg) {
                Log.i(TAG, msg.getPayload());
                switch (getMsgType(msg))
                {
                    case "RECEIVEDMESSAGE" -> {
                        MsgInfo msgInfo = deserializePayload(msg, MsgInfo.class);
                        handleChatMessage(msgInfo);
                    }
                    case "NEWCHAT" -> {
                        ChatInfo chatInfo = deserializePayload(msg, ChatInfo.class);
                        handleNewChat(chatInfo);
                    }
                }
            }
        };
    }

    private String getMsgType(StompMessage msg)
    {
        String body = msg.getPayload();
        JsonObject object = gson.fromJson(body, JsonObject.class);
        JsonElement element = object.get("msgType");
        String result = element.getAsString();
        return result;
    }

    public <T> T deserializePayload(StompMessage msg, Class<T> type)
    {
        String body = msg.getPayload();
        JsonObject object = gson.fromJson(body, JsonObject.class);
        JsonElement element = object.get("payload");
        T result = gson.fromJson(element, type);
        return result;
    }

    public <T> String serializeObject(T object)
    {
        return gson.toJson(object);
    }

    private void handleChatMessage(MsgInfo msg)
    {
        Log.i(TAG, "msg from: " + msg.login + ". They say: " + msg.text);
    }
    private void handleNewChat(ChatInfo chat)
    {
        Log.i(TAG, "new chat named: " + chat.getTitle());
    }

    public <T> T createRetrofit(Class<T> klass)
    {
        return retrofit.create(klass);
    }
    public <T> void sendStompMessage(String topic, T object)
    {
        String jsonifiedObject = serializeObject(object);
        stompClient.send(topic, jsonifiedObject).subscribe();
    }
    public void setUserStompConnection(long userid)
    {
        webClient.setStompLifeCycle(createConsumer(userid));
    }
    public void subscribe(String topic, Consumer<StompMessage> subConsumer)
    {
        /*if (!subscriptionsManager.consumerExists(topic)) {
            subscriptionsManager.addConsumer(topic);
            StompConsumer consumer = subscriptionsManager.getConsumer(topic);*/
            stompClient.topic(topic)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subConsumer);
        /*}
        else
        {
            StompConsumer consumer = subscriptionsManager.getConsumer(topic);
            consumer.addSubConsumer(subConsumer);
        }
        */
    }
}
