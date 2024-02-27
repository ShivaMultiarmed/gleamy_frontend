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
    @GET("/api/v1/chats/users/{userid}")
    Call<List<Chat>> getAllChats(@Path("userid") Long userid);
    @POST("/api/v1/chats/add")
    Call<Chat> addChat(@Body Chat chat);
    @POST("/gleamy/chats/{userid}/members")
    Call<List<User>> getChatMembers(@Path("userid") long chatid);
}
