package mikhail.shell.gleamy.api;

import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.OPENED;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.CLOSED;
import static ua.naiksoftware.stomp.dto.LifecycleEvent.Type.ERROR;

import android.app.Activity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.reactivestreams.Subscriber;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.function.Consumer;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import mikhail.shell.gleamy.activities.ChatActivity;
import mikhail.shell.gleamy.activities.ChatsList;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.MsgInfo;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class AppHttpClient{
    protected static OkHttpClient okHttpClient;
    protected static AppHttpClient client;
    protected final Retrofit retrofit;
    //@Getter
    //protected final WebSocket socket;
    //private Request request;
    private static final String host = "158.160.22.54";

    protected final StompClient stompClient;
    protected static Activity currentActivity;
    protected long userid;
    private final GsonBuilder gsonBuilder;
    private final Gson gson;
    private AppHttpClient()
    {
        gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Serializable.class,new InterfaceAdapter<Serializable>());
        gson = gsonBuilder.create();

        OkHttpClient.Builder okbuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okbuilder.addInterceptor(interceptor);

        okHttpClient = okbuilder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://"+host+":8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        /*request = new Request.Builder()
                .url("ws://"+host+":8080/gleamy/websocket")
                .build();
        socket = okHttpClient.newWebSocket(request, new AppWebSocketListener());*/
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP,
                "ws://" + host + ":8080/gleamy/websocket");
    }
    public void setUserid(long id)
    {
       userid = id;
    }

    public static AppHttpClient getClient(){
        if (client == null)
            client = new AppHttpClient();
        return client;
    }
    /*public WebSocket getSocket()
    {
        return socket;
    }*/

    public void setCurrentActivity(Activity activity)
    {
        currentActivity = activity;
    }
    public Activity getCurrentActivity()
    {
        return currentActivity;
    }

    /*public void subscribe(long userid)
    {
        String connectFrame = "CONNECT\n" +
                "accept-version:1.2\n" +
                "host:webBroker\n" +
                "\n" +
                "\u0000";
        getSocket().send(connectFrame);
        setUserid(userid);
        String stomp = "SUBSCRIBE\n" +
                "id:sub-"+userid+"\n" +
                "destination:/gleamy/topic/users/"+userid+"\n"+
                "\n"+
                "\u0000";
        getSocket().send(stomp);
    }*/

    public StompClient getStompClient()
    {
        return stompClient;
    }
    public Gson getGson() {
        return gson;
    }


    public void connect(long userid)
    {

        System.out.println("Trying to connect");
        stompClient.connect();

        stompClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    System.out.println("Stomp connection opened");
                    stompClient.topic("/topic/users/"+userid)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(msg -> {
                                Serializable receivedObject = gson.fromJson(msg.getPayload(), Serializable.class);

                                //String msgType = stompWrapper.getMsgType();
                                //Serializable receivedObject = stompWrapper.getPayload();
                                ChatsList list = (ChatsList) AbstractAPI.getActivity("ChatsList");
                                switch (receivedObject.getClass().getName())
                                {
                                    case "mikhail.shell.gleamy.models.MsgInfo":
                                        MsgInfo msgInfo = (MsgInfo) receivedObject;
                                        if (currentActivity instanceof ChatActivity)
                                        {
                                            ChatActivity chat = (ChatActivity) currentActivity;
                                            if (chat.getChatid() == msgInfo.chatid)
                                            {
                                                if (chat.getMsgs().isEmpty())
                                                    chat.clear();
                                                chat.getMsgs().put(msgInfo.msgid,chat.createMessage(msgInfo));
                                                chat.displayMessage(chat.getMessage(msgInfo.msgid));
                                            }
                                        }

                                        list.elevateChat(msgInfo.getChatid(), msgInfo);

                                        break;
                                    case "mikhail.shell.gleamy.models.ChatInfo":
                                        ChatInfo chatInfo = (ChatInfo) receivedObject;

                                        if (list.isEmpty())
                                            list.clear();
                                        list.addChat(chatInfo);

                                        stompClient.topic("/topic/chat/"+chatInfo.getId()).subscribe();

                                        break;
                                    default:
                                        break;
                                }
                            });
                    break;

                case ERROR:
                    lifecycleEvent.getException().printStackTrace(System.err);
                    break;

                case CLOSED:
                    System.out.println("Stomp connection closed");
                    break;
            }
        });



        //stompClient.send("/gleamy/topic/users/"+userid  , "My first STOMP message!").subscribe();
    }

    private class AppWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket socket, Response response)
        {
            System.out.println("socket is open");
            try {
                System.out.println(response.body().string());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        @Override
        public void onFailure(WebSocket socket, Throwable t, Response response)
        {
            System.out.println("Socket message: "+t.getMessage());
        }
        @Override
        public void onMessage(WebSocket socket, String message)
        {
            System.out.println("from socket" + message);
            /*String activityName = currentActivity.getLocalClassName();
            switch (activityName)
            {
                case "ChatsList" ->
                {

                }
                case "ChatActivity" ->
                {

                }
                default -> {

                }
            }*/
        }
        @Override
        public void onClosed(WebSocket socket, int code, String reason)
        {
            System.out.println("socket closing reason: " + reason);
        }
    }
    private final class InterfaceAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

        @Override
        public T deserialize(JsonElement jsonElement, Type type,
                             JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonPrimitive prim = (JsonPrimitive) jsonObject.get("msgType");
            String msgType = prim.getAsString();
            String prefix = "mikhail.shell.gleamy.models.";
            Class klass = getObjectClass( prefix +
                    switch (msgType)
                            {
                                case "RECEIVEDMESSAGE" -> "MsgInfo";
                                case "NEWCHAT" -> "ChatInfo";
                                default -> "";
                            }
            );
            return jsonDeserializationContext.deserialize(jsonObject.get("payload"), klass);
        }
        @Override
        public JsonElement serialize(T jsonElement, Type type, JsonSerializationContext jsonSerializationContext) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("msgType",
                    switch (jsonElement.getClass().getName())
                            {
                                case "mikhail.shell.gleamy.models.MsgInfo" -> "RECEIVEDMESSAGE" ;
                                case "mikhail.shell.gleamy.models.ChatInfo" -> "NEWCHAT";
                                default -> "";
                            });
            jsonObject.add("payload", jsonSerializationContext.serialize(jsonElement));
            return jsonObject;
        }

        public Class getObjectClass(String className) {
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonParseException(e.getMessage());
            }
        }
    }
}
