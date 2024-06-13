package mikhail.shell.gleamy.repositories;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import mikhail.shell.gleamy.api.MediaApi;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    public void fetchMediaPortionByUserId(Long userid, Type type, Long portion_num, MutableLiveData<List<Media>> mediaData)
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
    public void postMedia(Media media, byte[] bytes, MutableLiveData<Media> mediaData)
    {
        RequestBody mediaPart = objectToPart(media);
        MultipartBody.Part bytesPart = bytesToPart(bytes);
        Call<Media> request = mediaApi.postMedia(mediaPart, bytesPart);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Media> call, Response<Media> response) {
                if (response.code() == 200)
                    mediaData.postValue(response.body());
                else
                    mediaData.postValue(null);
            }

            @Override
            public void onFailure(Call<Media> call, Throwable t) {
                mediaData.postValue(null);
            }
        });
    }
    @NonNull
    private <T> RequestBody objectToPart(T object)
    {
        String json = webClient.serializeObject(object);
        MediaType type = MediaType.parse("application/json");
        return RequestBody.create(type,json);
    }
    @NonNull
    private MultipartBody.Part bytesToPart(byte[] bytes)
    {
        MediaType type = MediaType.parse("image/*");
        RequestBody byteRequestBody = RequestBody.create(bytes, type);
        // TODO: replace "something" with file extension
        return MultipartBody.Part.createFormData("file", "media.something", byteRequestBody);
    }
    public void removeMedia(String uuid, MutableLiveData<Boolean> removeData)
    {
        Call<Boolean> request = mediaApi.removeMedia(uuid);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<Boolean> call, @NonNull Response<Boolean> response) {
                removeData.postValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<Boolean> call, @NonNull Throwable t) {
                removeData.postValue(false);
            }
        });
    }
    public void getTotalMediaNumber(long userid, Type type, MutableLiveData<Long> quantityData)
    {
        final Call<Long> request = mediaApi.getTotalMediaNumber(userid, type);
        request.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                quantityData.postValue(response.body());
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                quantityData.postValue(null);
            }
        });
    }
}
