package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.activities.ChatActivity;
import mikhail.shell.gleamy.models.MsgInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MsgAPIClient extends AbstractAPI{
    private final AppHttpClient httpClient;
    private static MsgAPIClient client;
    private final MsgApi msgApi;
    private final Map<String, Activity> activities;
    private MsgInfo curMsg;

    private MsgAPIClient()
    {
        httpClient = AppHttpClient.getClient();
        msgApi = httpClient.retrofit.create(MsgApi.class);
        activities = new HashMap<>();
    }
    public static MsgAPIClient getClient()
    {
        if (client == null)
            client = new MsgAPIClient();
        return client;
    }
    public void addActivities(String name, Activity activity)
    {
        activities.put(name, activity);
    }
    public void getChatMessages(long chatid)
    {
        ChatActivity chatActivity = (ChatActivity) activities.get("ChatActivity");
        Call<Map<Long, MsgInfo>> call = msgApi.getChatMsgs(chatid);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<Long, MsgInfo>> call, Response<Map<Long, MsgInfo>> response) {
                Map<Long, MsgInfo> msgs = response.body();
                chatActivity.viewAllMessages(msgs);
            }

            @Override
            public void onFailure(Call<Map<Long, MsgInfo>> call, Throwable t) {

            }
        });
    }
    public void sendMessage(MsgInfo msg)
    {
        ChatActivity chatActivity = (ChatActivity) activities.get("ChatActivity");
        Call<Map<String, Long>> call = msgApi.sendMessage(msg);
        curMsg = msg;
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {
                long msgid = response.body().get("msgid");
                System.out.println("created msg: " + msgid);
                curMsg.setMsgid(msgid);

                chatActivity.getMsgs().put(msgid,chatActivity.createMessage(curMsg));
                chatActivity.displayMessage(chatActivity.getMessage(msgid));
                chatActivity.getChatTextArea().setText("");
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {

            }
        });
    }
}
