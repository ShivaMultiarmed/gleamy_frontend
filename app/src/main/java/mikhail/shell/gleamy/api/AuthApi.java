package mikhail.shell.gleamy.api;



import mikhail.shell.gleamy.models.Post;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthApi {
    @GET("/auth/login")
    Call<String> login(@Query("login") String login, @Query("password") String password);
    @GET("/auth/signup")
    Call<String> signup(@Query("login") String login, @Query("password") String password, @Query("email") String email);

    @GET("/posts/1")
    Call<Post> test();
}
