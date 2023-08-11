package mikhail.shell.gleamy.api;

import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.models.UserInfo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {
    @GET("/gleamy/users/search/{login}")
    public Call<Map<Long,UserInfo>> getUsersByLogin(@Path("login") String login);
}
