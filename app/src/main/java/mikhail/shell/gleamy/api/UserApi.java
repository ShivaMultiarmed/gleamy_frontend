package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface UserApi {
    @GET("/gleamy/users/search/{login}")
    Call<Map<Long, User>> getUsersByLogin(@Path("login") String login);
    @GET("/gleamy/users/{id}/avatar")
    Call<byte[]> getUserAvatar(@Path("id") long userid);
    @GET("/gleamy/users/avatars")
    Call<List<byte[]>> getUsersAvatars(@Body List<Long> usersids);
    @GET("/users/{id}")
    Call<User> getUserById(@Path("id") Long userid);
}
