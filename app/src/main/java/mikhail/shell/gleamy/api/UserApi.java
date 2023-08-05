package mikhail.shell.gleamy.api;

import java.util.List;

import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/users/find/login/{login}")
    public Call<List<UserInfo>> getUsersByLogin(@Query("login") String login);
}
