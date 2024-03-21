package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.ActionModel;
import mikhail.shell.gleamy.models.User;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("users/search")
    Call<List<User>> getUsersByLogin(@Query("login") String login);
    @GET("users/{id}/avatar")
    Call<ActionModel<byte[]>> getUserAvatar(@Path("id") long userid);
    @GET("users/avatars")
    Call<List<byte[]>> getUsersAvatars(@Body List<Long> usersids);
    @GET("users/{id}")
    Call<User> getUserById(@Path("id") Long userid);
    @Multipart
    @PATCH("users/{userid}/avatar")
    Call<Map<String, Object>> editAvatarByUserId(@Path("userid") Long userid, @Part MultipartBody.Part avatar);
}
