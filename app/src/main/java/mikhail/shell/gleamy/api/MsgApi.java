package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MsgApi
{
    @GET("/api/v1/messages/fromchat/{chatid}")
    Call<List<Message>> getChatMsgs(@Path("chatid") long chatid);

    @POST("/api/v1/messages/add")
    Call<Message> sendMessage(@Body Message msg);
}
