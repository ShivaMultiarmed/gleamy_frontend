package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.activities.LogInActivity;
import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.services.MessagesService;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPref;
    private WebClient webClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webClient = WebClient.getInstance(this);
        initPref();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        long userid = sharedPref.getLong("userid", 0);
        if (userid != 0)
        {
            webClient.connectToStomp();
            webClient.setUserStompConnection(userid);
            startMessageService();
            goToChats(userid);
        }
        else
            goToAuth();
    }
    private void initPref()
    {
        sharedPref = getSharedPreferences("authdetails", MODE_PRIVATE);
    }
    private void goToChats(long userid)
    {
        Intent intent = new Intent(this, ChatsListActivity.class);
        GleamyApp.getApp().setUser(new User(userid));
        startActivity(intent);
        finish();
    }
    private void goToAuth()
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
    private void startMessageService()
    {
        Intent intent = new Intent(this, MessagesService.class);
        startService(intent);
    }
}