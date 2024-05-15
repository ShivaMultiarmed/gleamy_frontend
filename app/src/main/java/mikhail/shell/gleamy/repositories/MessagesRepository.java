package mikhail.shell.gleamy.repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mikhail.shell.gleamy.api.MsgApi;
import mikhail.shell.gleamy.api.UserApi;
import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.Message;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesRepository extends AbstractRepository {
    private final static String TAG = MessagesRepository.class.getName();
    private static MessagesRepository instance;
    private MsgApi msgApi;
    private UserApi userApi;

    private MessagesRepository(Context context) {
        super(context);
        msgApi = webClient.createApi(MsgApi.class);
    }
    public static MessagesRepository getInstance(Context context)
    {
        if (instance == null)
            instance = new MessagesRepository(context);
        return instance;
    }
    public void fetchAllMessages(MutableLiveData<Map<Long, Message>> msgsData, long chatid)
    {
        Call<List<Message>> call = msgApi.getChatMsgs(chatid);
        call.enqueue(
                new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                        List<Message> msgsList = response.body();
                        Map<Long, Message> msgMap = new LinkedHashMap<>();
                        if (msgsList != null)
                            if (!msgsList.isEmpty())
                                for (Message msg: msgsList)
                                    if (msg.getDateTime() != null)
                                        msgMap.put(msg.getMsgid(), msg);

                        msgsData.postValue(msgMap);
                    }

                    @Override
                    public void onFailure(Call<List<Message>> call, Throwable t) {
                        msgsData.postValue(null); // handle error via toast in activity
                    }
                }
        );
    }
    public void sendMessage(Message msg)
    {
        Call<Message> request = msgApi.sendMessage(msg);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }
    public void observeIncomingMessage(MutableLiveData<ActionModel<Message>> msgData, long chatid)
    {
        webClient.observeSubscription("/topics/chats/" + chatid,
                message -> {
                    Message msg = webClient.deserializePayload(message, Message.class);
                    msgData.postValue(new ActionModel<>("RECEIVEDMESSAGE", msg));
                }
        );
    }

}
