package mikhail.shell.gleamy.api;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.time.LocalDateTime;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import mikhail.shell.gleamy.api.json.adapters.DateTimeAdapter;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
    private CompositeDisposable compositeDisposable;
    private final Context appContext;
    private WebClient(Context appContext)
    {
        this.appContext = appContext;

        initGson();
        initOkHttpClient();
        initRetrofit();

        initStompClient();
        initSubscriptionsManager();
    }

    public static WebClient getInstance(Context context)
    {
        if (webClient == null)
            webClient = new WebClient(context.getApplicationContext());
        return webClient;
    }

    private void initOkHttpClient()
    {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new HttpLoggingInterceptor());
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
    public void connectToStomp()
    {
        compositeDisposable = new CompositeDisposable();
        stompClient.connect();
        stompClient.withClientHeartbeat(HEARTBEAT).withClientHeartbeat(HEARTBEAT);
    }
    public void reconnectToStomp()
    {
        connectToStomp();
        resetSubscriptions();
    }
    public void disconnectFromStomp()
    {
        removeSubscriptions();
        stompClient.disconnect();
    }
    private void setStompLifeCycle(Consumer<LifecycleEvent> consumer)
    {
        compositeDisposable.add(stompClient.lifecycle().subscribe(consumer));
    }
    private Consumer<LifecycleEvent> createConsumer(long userid)
    {
        return lifecycleEvent -> {
            switch (lifecycleEvent.getType())
            {
                case OPENED -> onStompOpened(lifecycleEvent, userid);
                case CLOSED -> onStompClosed(lifecycleEvent);
                case ERROR -> onStompError(lifecycleEvent);
                case FAILED_SERVER_HEARTBEAT -> onFailedHeartbeat(lifecycleEvent);
            }
        };
    }
    private void onStompOpened(LifecycleEvent e, long userid)
    {
        //subscribeToUserTopic(userid);
        Log.i(TAG, "stomp is opened");
    }
    private void onStompClosed(LifecycleEvent e) {
        Log.e(TAG, "stomp is closed");
        Log.i(TAG, "trying to reconnect to stomp");
        reconnectToStomp();
    }
    private void onStompError(LifecycleEvent e) {
        Log.e(TAG, "error with stomp");
    }
    private void onFailedHeartbeat(LifecycleEvent e) {
        Log.e(TAG, "failed hearbeat");
    }

    private void subscribeToUserTopic(long userid)
    {
        stompClient.topic("/topic/users/" + userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(createMessageHandler());
    }

    private Consumer<StompMessage> createMessageHandler() {
        return stompMsg -> {

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

    private void handleChatMessage(Message msg)
    {
        Log.i(TAG, "msg from: " + msg.login + ". They say: " + msg.text);
    }
    private void handleNewChat(Chat chat)
    {
        Log.i(TAG, "new chat named: " + chat.getTitle());
    }

    public <T> T createApi(Class<T> klass)
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
    private void subscribe(String topic)
    {
        subscriptionsManager.addConsumer(topic);
        StompConsumer consumer = subscriptionsManager.getConsumer(topic);
        subscribe(topic, consumer);
    }
    private void subscribe(String topic, StompConsumer consumer)
    {
        Disposable disposable = stompClient.topic(topic)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
        compositeDisposable.add(disposable);
    }
    public void observeSubscription(String topic, Consumer<StompMessage> subConsumer)
    {
        if (!subscriptionsManager.consumerExists(topic))
            subscribe(topic);
        StompConsumer consumer = subscriptionsManager.getConsumer(topic);
        consumer.addSubConsumer(subConsumer);
    }
    private void unsubscribe(String topic)
    {
        stompClient.topic(topic)
                .unsubscribeOn(Schedulers.io())
                .subscribe();
        subscriptionsManager.removeConsumer(topic);
    }
    private void resetSubscriptions()
    {
        subscriptionsManager.getConsumers().forEach(webClient::subscribe);
        Log.i(TAG, "subscriptions are reset");
    }
    public void removeSubscriptions()
    {
        compositeDisposable.dispose();
        subscriptionsManager.removeAllConsumers();
    }
}
