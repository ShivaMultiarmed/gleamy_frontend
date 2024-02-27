package mikhail.shell.gleamy.api;


import java.util.Map;

import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/api/v1/auth/login")
    Call<User> login(@Query("login") String login, @Query("password") String password);
    @POST("/api/v1/auth/signup")
    Call<User> signup(@Body User user);
}
