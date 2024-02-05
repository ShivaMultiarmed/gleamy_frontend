package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ChatApi {
    @GET("/gleamy/chats/users/{userid}")
    Call<Map<Long, Chat>> getAllChats(@Path("userid") long userid);
    @POST("/gleamy/chats/add")
    Call<Map<String, String>> addChat(@Body Chat chat);
    @POST("/gleamy/chats/{userid}/members")
    Call<List<User>> getChatMembers(@Path("userid") long chatid);
}
