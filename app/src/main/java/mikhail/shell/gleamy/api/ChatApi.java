package mikhail.shell.gleamy.api;

import java.util.List;

import mikhail.shell.gleamy.models.ChatInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ChatApi {
    @GET("/{userid}/chats/")
    public Call<List<ChatInfo>> getAllChats(@Query("userid") long userid);
}
