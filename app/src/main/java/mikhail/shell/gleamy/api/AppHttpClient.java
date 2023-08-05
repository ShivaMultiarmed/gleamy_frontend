package mikhail.shell.gleamy.api;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppHttpClient{
    protected static AppHttpClient client;
    protected Retrofit retrofit;
    protected AppHttpClient()
    {
        retrofit = new Retrofit.Builder()
                //.baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("http://192.168.137.1:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public static AppHttpClient getClient(){
        if (client == null)
            client = new AppHttpClient();
        return client;
    }
}
