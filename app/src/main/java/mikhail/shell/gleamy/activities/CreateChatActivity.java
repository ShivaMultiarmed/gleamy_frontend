package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.ChatAPIClient;
import mikhail.shell.gleamy.api.UserAPIClient;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;

public class CreateChatActivity extends AppCompatActivity {
    private long userid;
    private LinearLayout layout;

    private Map<Long,User> users, chosen;
    private EditText chatNameInput, searchInput;
    private ImageView createChatBtn, searchBtn;

    private UserAPIClient userClient;
    private ChatAPIClient chatClient;
    private ChatInfo chatInfo;

    public CreateChatActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_chat_activity);

        init();
    }

    private void init()
    {
        Bundle b = getIntent().getExtras();
        userid = b.getLong("userid");

        chatInfo = new ChatInfo();
        chatInfo.addMember(userid);

        userClient = UserAPIClient.getClient();
        userClient.addActivity("CreateChatActivity", this);

        chatClient = ChatAPIClient.getClient();
        chatClient.addActivity("CreateChatActivity", this);

        layout = findViewById(R.id.addUsersToChat);

        chatNameInput = findViewById(R.id.chatTitleInput);
        createChatBtn = findViewById(R.id.createChatBtn);
        createChatBtn.setOnClickListener(e->{

            chatInfo.setTitle(chatNameInput.getText().toString());
            chatClient.addChat(chatInfo);
        });

        searchInput = findViewById(R.id.searchUsersInput);
        searchBtn = findViewById(R.id.searchUsersBtn);
        searchBtn.setOnClickListener(e->{
            userClient.getUsersByLogin(searchInput.getText().toString());
        });
        users = null; //createUsers(new ArrayList<UserInfo>());
        //displayUsers();
    }
    public void createUsers(Map<Long,UserInfo> infos)
    {
        users = new HashMap<>();
        for (UserInfo info : infos.values())
        {
            users.put(info.getId(),createUser(info));
        }

    }
    public void displayUsers()
    {
        layout.removeAllViews();
        for (User user : users.values())
        {
            displayUser(user);
        }
    }
    private User createUser(UserInfo info)
    {
        User user = (User)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        user.init();
        user.setUser(info);
        return user;
    }
    public void displayUser(User user)
    {
        layout.addView(user);
    }
    public void setChooseListener()
    {
        for (User user : users.values())
            user.setOnClickListener(view->{
                User curUser = (User) view;
                curUser.setBackgroundColor(getResources().getColor(R.color.input_background));
                chatInfo.addMember(curUser.getUser().getId());
            });
    }
}