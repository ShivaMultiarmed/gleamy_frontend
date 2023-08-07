package mikhail.shell.gleamy.api;

import mikhail.shell.gleamy.models.UserInfo;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Query;

public class AuthAPIClient extends  AbstractAPI{

    private static AuthAPIClient client;
    private AuthApi authApi;
    private String code;
    protected AuthAPIClient()
    {

        authApi = httpClient.retrofit.create(AuthApi.class);
    }
    public static AuthAPIClient getClient() {
        if (client == null)
            client = new AuthAPIClient();
        return client;
    }
    public String login(String login, String password)
    {
        System.out.println("Validation start");
        /*Call<String> call = authApi.login(login, password);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("HERE IS a RESPONSE" + response.toString());
                if (response.isSuccessful())
                    code = response.body();
                else
                    code = "ERROR";
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("HERE IS an ERROR" + call.toString() + " | " +t.getMessage());

                code = "ERROR";
            }
        });*/
        code = "OK";
        return code;
    }
    public String signup( String login, String password, String email)
    {
        Call<String> call = authApi.signup( login,  password, email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                System.out.println("HERE IS a RESPONSE" + response.toString());
                if (response.isSuccessful())
                    code  = response.body();
                else
                    code = "ERROR";
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("HERE IS an ERROR" + call.toString() + " | " +t.getMessage());

                code = "ERROR";
            }
        });
        return code;
    }
    public String test()
    {
        Call<UserInfo> call = authApi.test();
        Headers hs = call.request().headers();

        for (String header : hs.names())
            System.out.println(header + ": " + hs.get(header));
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                code = response.body().getLogin();

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                code = t.getMessage() + "\n";

                Headers hs = call.request().headers();
                for (String header : hs.names())
                    code += header + "\n";
                System.out.println(code + "headers are " + (hs==null));
            }
        });
        return code;
    }
}
