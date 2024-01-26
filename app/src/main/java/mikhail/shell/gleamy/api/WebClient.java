package mikhail.shell.gleamy.api;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
    private WebClient()
    {
        initGson();
        initOkHttpClient();
        initRetrofit();

        initStompClient();
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
    private void setStompLifeCycle(Consumer<? super LifecycleEvent> consumer)
    {
        stompClient.lifecycle().subscribe(consumer);
    }
    private Consumer<LifecycleEvent> createConsumer(long userid)
    {
        return new Consumer<LifecycleEvent>() {
            @Override
            public void accept(LifecycleEvent lifecycleEvent) throws Exception {

                switch (lifecycleEvent.getType())
                {
                    case OPENED -> onStompOpened(userid);
                    case CLOSED -> onStompClosed();
                    case ERROR -> onStompError();
                    case FAILED_SERVER_HEARTBEAT -> onFailedHearbeat();
                }

            }
        };
    }
    private void onStompOpened(long userid)
    {
        subscribeToUserTopic(userid);
    }
    private void onStompClosed() {}
    private void onStompError() {}
    private void onFailedHearbeat() {}

    private void subscribeToUserTopic(long userid)
    {
        stompClient.topic("/topics/users/" + userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createMessageHandler());
    }

    private Consumer<? super StompMessage> createMessageHandler() {
        return new Consumer<StompMessage>() {
            @Override
            public void accept(StompMessage msg) throws Exception {
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
        JsonElement element = gson.toJsonTree(body);
        JsonObject object = element.getAsJsonObject();
        return object.get("msgType").getAsString();
    }

    public <T> T deserializePayload(StompMessage msg, Class<T> type)
    {
        String body = msg.getPayload();
        JsonElement element = gson.toJsonTree(body);
        JsonObject object = element.getAsJsonObject();
        String payload = object.get("payload").getAsString();
        return gson.fromJson(payload, type);
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
}
