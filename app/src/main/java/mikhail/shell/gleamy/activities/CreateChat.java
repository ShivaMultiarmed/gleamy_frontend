package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.UserAPIClient;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;

public class CreateChat extends AppCompatActivity {

    private LinearLayout layout;

    private List<User> users;
    private EditText searchInput;
    private ImageView searchBtn;

    private UserAPIClient userClient;

    public CreateChat() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat_activity);

        init();
    }

    private void init()
    {
        userClient = UserAPIClient.getClient();

        layout = findViewById(R.id.addUsersToChat);
        searchInput = findViewById(R.id.searchUsersInput);
        searchBtn = findViewById(R.id.searchUsersBtn);
        searchBtn.setOnClickListener(e->{
            users = createUsers(userClient.getUsersByLogin(searchInput.getText().toString()));
            displayUsers();
        });
        users = null; //createUsers(new ArrayList<UserInfo>());
        //displayUsers();
    }
    private List<User> createUsers(List<UserInfo> infos)
    {
        List<User> users = new ArrayList<>();
        for (UserInfo info : infos)
        {
            users.add(createUser(info));
        }

        return users;
    }
    private void displayUsers()
    {
        for (User user : users)
        {
            displayUser(user);
        }
    }
    private User createUser(UserInfo info)
    {
        User user = (User)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        user.init();
        user.setUser(info);
        return user;//new UserInfo("Barry", "flash123")
    }
    public void displayUser(User user)
    {
        layout.addView(user);
    }
}