package mikhail.shell.gleamy.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import mikhail.shell.gleamy.api.MediaApi;
import mikhail.shell.gleamy.models.Media;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaRepository extends AbstractRepository {
    private static MediaRepository repository;
    private MediaApi mediaApi;
    private MediaRepository(Context context) {
        super(context);
        initApi();
    }
    private void initApi()
    {
        mediaApi = webClient.createApi(MediaApi.class);
    }
    public static MediaRepository getInstance(Context context)
    {
        if (repository == null)
            repository = new MediaRepository(context);
        return repository;
    }
    public void fetchMediaPortionByUserId(Long userid, Media.Type type, Long portion_num, MutableLiveData<List<Media>> mediaData)
    {
        Call<List<Media>> request = mediaApi.getMediaPortionByUserId(userid, portion_num, type);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<List<Media>> call, @NonNull Response<List<Media>> response) {
                if (response.code() == 200)
                    mediaData.postValue(response.body());
                response.raw().toString();
            }

            @Override
            public void onFailure(@NonNull Call<List<Media>> call, @NonNull Throwable t) {
                mediaData.postValue(null);
            }
        });
    }
    public void fetchMediaById(String mediaid, MutableLiveData<byte[]> mediaData)
    {
        Call<ResponseBody> request = mediaApi.getMediaById(mediaid);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 200) {
                    try {
                        mediaData.postValue(response.body().bytes());
                    } catch (IOException e) {
                        mediaData.postValue(null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                mediaData.postValue(null);
            }
        });
    }
}
