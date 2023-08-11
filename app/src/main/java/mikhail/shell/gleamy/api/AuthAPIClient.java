package mikhail.shell.gleamy.api;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.activities.ChatsList;
import mikhail.shell.gleamy.activities.LogIn;
import mikhail.shell.gleamy.activities.SignUp;
import mikhail.shell.gleamy.models.UserInfo;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthAPIClient extends  AbstractAPI{

    private static AuthAPIClient client;
    private AuthApi authApi;
    private String code;
    private AuthAPIClient()
    {
        authApi = httpClient.retrofit.create(AuthApi.class);
    }
    public static AuthAPIClient getClient() {
        if (client == null)
            client = new AuthAPIClient();
        return client;
    }

    public void login(String login, String password)
    {
        LogIn loginActivity = (LogIn)activities.get("LogIn");
        Call<Map<String, String>> call = authApi.login(login, password);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> response) {
                System.out.println("HERE IS a RESPONSE" + response.toString());
                if (response.isSuccessful())
                    code = response.body().get("code");
                else
                    code = "ERROR";
                loginActivity.displayMessage(code);
                if (code.equals("OK"))
                {
                    Intent toChatsList = new Intent(loginActivity, ChatsList.class);
                    Bundle b = new Bundle();
                    b.putLong("userid", Long.parseLong(response.body().get("userid")));
                    toChatsList.putExtras(b);
                    loginActivity.startActivity(toChatsList);
                }
            }

            @Override
            public void onFailure(Call<Map<String, String>> call, Throwable t) {
                System.out.println("HERE IS an ERROR" + call.toString() + " | " +t.getMessage());

                code = "ERROR";
                loginActivity.displayMessage(code);
            }
        });
    }
    public void signup( String login, String password, String email)
    {
        SignUp signUpActivity = (SignUp) activities.get("SignUp");
        Call<Map<String, Object>> call = authApi.signup( login,  password, email);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful())
                    code  = (String)response.body().get("code");
                else
                    code = "ERROR";

                signUpActivity.displayMessage(code);

                if (code.equals("OK"))
                {
                    Intent redirect = new Intent(signUpActivity, ChatsList.class);
                    Bundle b = new Bundle();

                    b.putLong("userid", Long.parseLong((response.body().get("userid").toString())));
                    redirect.putExtras(b);
                    signUpActivity.startActivity(redirect);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                code = "ERROR";
            }
        });
    }

}
