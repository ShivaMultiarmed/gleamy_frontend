package mikhail.shell.gleamy.api;



import mikhail.shell.gleamy.models.Post;
import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface AuthApi {
    @GET("/gleamy/auth/login")
    Call<String> login(@Query("login") String login, @Query("password") String password);
    @GET("/gleamy/auth/signup")
    Call<String> signup(@Query("login") String login, @Query("password") String password, @Query("email") String email);

    @GET("/gleamy/auth/test")
    @Headers("User-Agent: Mozilla/5.0 (Android 10; Mobile; rv:92.0) Gecko/92.0 Firefox/92.0")
    Call<UserInfo> test();
}
