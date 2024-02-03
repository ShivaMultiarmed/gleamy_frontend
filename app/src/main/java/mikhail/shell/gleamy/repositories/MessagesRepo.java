package mikhail.shell.gleamy.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mikhail.shell.gleamy.api.MsgApi;
import mikhail.shell.gleamy.models.MsgInfo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesRepo extends AbstractRepo {
    private final static String TAG = MessagesRepo.class.getName();
    private MsgApi msgApi;

    public MessagesRepo() {
        super();
        msgApi = webClient.createRetrofit(MsgApi.class);
    }
    public void fetchAllMessages(MutableLiveData<Map<Long, MsgInfo>> msgsData, long chatid)
    {
        Call<List<MsgInfo>> call = msgApi.getChatMsgs(chatid);
        call.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<MsgInfo>> call, Response<List<MsgInfo>> response) {
                        List<MsgInfo> msgsList = response.body();
                        Map<Long, MsgInfo> msgMap = new LinkedHashMap<>();
                        if (msgsList != null && !msgsList.isEmpty())
                            for (MsgInfo msg: msgsList)
                                if (msg.getDateTime() != null)
                                    msgMap.put(msg.msgid, msg);
                        msgsData.postValue(msgMap);
                        observeIncomingMessage(msgsData, chatid);
                    }

                    @Override
                    public void onFailure(Call<List<MsgInfo>> call, Throwable t) {
                        msgsData.postValue(null); // handle error via toast in activity
                    }
                }
        );
    }
    public void sendMessage(MsgInfo msg)
    {
        Call<Map<String, Long>> request = msgApi.sendMessage(msg);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Long>> call, Response<Map<String, Long>> response) {

            }

            @Override
            public void onFailure(Call<Map<String, Long>> call, Throwable t) {

            }
        });
    }
    public void observeIncomingMessage(MutableLiveData<Map<Long, MsgInfo>> msgsData, long chatid)
    {
        webClient.observeSubscription("/topic/chats/" + chatid,
                message -> {
                    Map<Long, MsgInfo> msgsMap = msgsData.getValue();
                    MsgInfo msg = webClient.deserializePayload(message, MsgInfo.class);
                    msgsMap.put(msg.msgid, msg);
                    msgsData.postValue(msgsMap);
                    Log.i(TAG, "Msg text here: "+msg.getText());
                }
        );
    }
}
