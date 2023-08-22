package mikhail.shell.gleamy.api;



import android.app.Activity;

import java.io.IOException;

import lombok.Getter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppHttpClient{
    protected static OkHttpClient okHttpClient;
    protected static AppHttpClient client;
    protected final Retrofit retrofit;
    @Getter
    protected final WebSocket socket;
    private Request request;
    private static final String host = "158.160.22.54";
    protected Activity currentActivity;
    protected long userid;
    private AppHttpClient()
    {
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
        request = new Request.Builder()
                .url("ws://"+host+":8080/gleamy/websocket")
                .build();
        socket = okHttpClient.newWebSocket(request, new AppWebSocketListener());
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
    public WebSocket getSocket()
    {
        return socket;
    }

    public void setCurrentActivity(Activity activity)
    {
        currentActivity = activity;
    }
    public Activity getCurrentActivity()
    {
        return currentActivity;
    }

    public void subscribe(long userid)
    {
        setUserid(userid);
        String stomp = "SUBSCRIBE\n" +
                "id:sub-"+userid+"\n" +
                "destination:/topic/users/"+userid+"\n"+
                "\n"+
                "\u0000";
        getSocket().send(stomp);
        getSocket().send("Hello, websocket");
    }

    private class AppWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket socket, Response response)
        {
            System.out.println("socket is open");
            System.out.println(response.body());

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
            String activityName = currentActivity.getLocalClassName();
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
            }
        }
        @Override
        public void onClosed(WebSocket socket, int code, String reason)
        {
            System.out.println("socket closing reason: " + reason);
        }
    }
}
