package mikhail.shell.gleamy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import mikhail.shell.gleamy.activities.ChatActivity;
import mikhail.shell.gleamy.activities.ChatsList;
import mikhail.shell.gleamy.activities.CreateChat;
import mikhail.shell.gleamy.activities.LogIn;
import mikhail.shell.gleamy.activities.SignUp;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, CreateChat.class);
        startActivity(intent);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}