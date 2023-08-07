package mikhail.shell.gleamy.api;



import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppHttpClient{
    protected static OkHttpClient okHttpClient;
    protected static AppHttpClient client;
    protected Retrofit retrofit;
    protected AppHttpClient()
    {
        OkHttpClient.Builder okbuilder = new OkHttpClient.Builder();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okbuilder.addInterceptor(interceptor);

        okHttpClient = okbuilder.build();
        retrofit = new Retrofit.Builder()
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("http://158.160.17.122:8080/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static AppHttpClient getClient(){
        if (client == null)
            client = new AppHttpClient();
        return client;
    }
}
