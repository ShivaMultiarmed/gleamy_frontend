package mikhail.shell.gleamy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import mikhail.shell.gleamy.activities.LogIn;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, LogIn.class);
        startActivity(intent);
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
}