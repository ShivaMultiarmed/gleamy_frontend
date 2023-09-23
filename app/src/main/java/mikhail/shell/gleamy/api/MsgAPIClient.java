package mikhail.shell.gleamy.api;

import android.app.Activity;

import java.util.HashMap;
import java.util.List;
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
    private MsgInfo curMsg;

    private MsgAPIClient()
    {
        httpClient = AppHttpClient.getClient();
        msgApi = httpClient.retrofit.create(MsgApi.class);
    }
    public static MsgAPIClient getClient()
    {
        if (client == null)
            client = new MsgAPIClient();
        return client;
    }

    public void getChatMessages(long chatid)
    {
        ChatActivity chatActivity = (ChatActivity) activities.get("ChatActivity");
        Call<List<MsgInfo>> call = msgApi.getChatMsgs(chatid);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<MsgInfo>> call, Response<List<MsgInfo>> response) {
                List<MsgInfo> msgs = response.body();
                chatActivity.viewAllMessages(msgs);
            }

            @Override
            public void onFailure(Call<List<MsgInfo>> call, Throwable t) {

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
                /*long msgid = response.body().get("msgid");
                System.out.println("created msg: " + msgid);
                curMsg.setMsgid(msgid);

                chatActivity.getMsgs().put(msgid,chatActivity.createMessage(curMsg));
                chatActivity.displayMessage(chatActivity.getMessage(msgid));*/
                chatActivity.getChatTextArea().setText("");
            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {

            }
        });
    }
}
