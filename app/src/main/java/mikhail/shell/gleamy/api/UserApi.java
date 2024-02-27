package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/api/v1/users/search")
    Call<List<User>> getUsersByLogin(@Query("login") String login);
    @GET("/api/v1/users/{id}/avatar")
    Call<ActionModel<byte[]>> getUserAvatar(@Path("id") long userid);
    @GET("/gleamy/users/avatars")
    Call<List<byte[]>> getUsersAvatars(@Body List<Long> usersids);
    @GET("/api/v1/users/{id}")
    Call<User> getUserById(@Path("id") Long userid);
}
