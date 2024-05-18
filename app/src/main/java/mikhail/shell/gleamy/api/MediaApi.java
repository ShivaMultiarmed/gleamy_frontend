package mikhail.shell.gleamy.api;

import java.util.List;

import mikhail.shell.gleamy.models.Media;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @Multipart
    @POST("users/media/post")
    Call<Media> postMedia(@Part MultipartBody.Part media, @Part MultipartBody.Part mediaBytes);
    @DELETE("users/media/{mediaid}")
    Call<Boolean> removeMedia(@Path("mediaid") String uuid);
}
