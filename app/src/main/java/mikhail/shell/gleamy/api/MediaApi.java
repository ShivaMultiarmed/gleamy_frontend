package mikhail.shell.gleamy.api;

import java.util.List;

import mikhail.shell.gleamy.models.Media;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MediaApi {
    @GET("users/media")
    Call<ResponseBody> getMediaById(@Query("uuid") String uuid);
    @GET("users/{userid}/media")
    Call<List<Media>> getMediaPortionByUserId(
            @Path("userid") Long userid,
            @Query("portion_num") Long portion_num,
            @Query("type") Media.Type type);
}
