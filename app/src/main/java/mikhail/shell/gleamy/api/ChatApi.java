package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.ChatInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ChatApi {
    @GET("/gleamy/chats/users/{userid}")
    Call<Map<Long,ChatInfo>> getAllChats(@Path("userid") long userid);
}
