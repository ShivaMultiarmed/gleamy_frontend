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
    @GET("/gleamy/messages/chat/{chatid}")
    Call<List<Message>> getChatMsgs(@Path("chatid") long chatid);

    @POST("/gleamy/messages/add")
    Call<Map<String, Long>> sendMessage(@Body Message msg);
}
