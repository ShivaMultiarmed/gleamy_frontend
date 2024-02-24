package mikhail.shell.gleamy;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;

import mikhail.shell.gleamy.activities.LogInActivity;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.User;

public class GleamyApp extends Application {

    private WebClient webClient;
    private Activity currentActivity;
    private User user;

    private static GleamyApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        webClient = WebClient.getInstance(this);
        //if (!false) // to do: if client is NOT logged in
            //startLoginActivity();
    }

    public static GleamyApp getApp()
    {
        return app;
    }

    private void startLoginActivity()
    {
        Intent loginIntent = new Intent(this, LogInActivity.class);
        startActivity(loginIntent);
    }

    public void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }
    public void initWebClient()
    {
        //webClient = WebClient.getInstance();
    }

    public User getUser()
    {
        return user;
    }
    public void setUser(User user) { this.user = user;}

    public Activity getCurrentActivity()
    {
        return currentActivity;
    }
    @Override
    public void onTerminate()
    {
        super.onTerminate();
    }
}
