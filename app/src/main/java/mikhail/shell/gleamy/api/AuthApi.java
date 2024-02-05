package mikhail.shell.gleamy.api;



import java.util.Map;

import mikhail.shell.gleamy.models.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthApi {
    @POST("/gleamy/auth/login")
    Call<Map<String, String>> login(@Query("login") String login, @Query("password") String password);
    @POST("/gleamy/auth/signup")
    Call<Map<String, Object>> signup(@Query("login") String login, @Query("password") String password, @Query("email") String email);

    @GET("/gleamy/auth/test")
    //@Headers("UserView-Agent: Mozilla/5.0 (Android 10; Mobile; rv:92.0) Gecko/92.0 Firefox/92.0")
    Call<User> test();
}
