package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.ChatAPIClient;
import mikhail.shell.gleamy.api.UserAPIClient;
import mikhail.shell.gleamy.databinding.CreateChatActivityBinding;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;

public class CreateChatActivity extends AppCompatActivity {
    private CreateChatActivityBinding B;
    private long userid;
    private Map<Long,User> users;
    private UserAPIClient userClient;
    private ChatAPIClient chatClient;
    private ChatInfo chatInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = CreateChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        init();
    }

    private void init()
    {
        Bundle b = getIntent().getExtras();
        userid = b.getLong("userid");

        chatInfo = new ChatInfo();
        chatInfo.addMember(userid);

        AbstractAPI.addActivity("CreateChatActivity", this);

        userClient = UserAPIClient.getClient();

        chatClient = ChatAPIClient.getClient();

        B.createChatBtn.setOnClickListener(e->{

            chatInfo.setTitle(B.chatTitleInput.getText().toString());
            chatClient.addChat(chatInfo);
        });


        B.searchUsersBtn.setOnClickListener(e->{
            userClient.getUsersByLogin(B.searchUsersInput.getText().toString());
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
        clear();
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
        B.addUsersToChat.addView(user);
    }
    public void clear()
    {
        B.addUsersToChat.removeAllViews();
    }
    public void setChooseListener()
    {
        for (User user : users.values())
            user.setOnClickListener(view->{
                User curUser = (User) view;
                long userid = curUser.getUser().getId();
                if (!chatInfo.hasMember(userid))
                {
                    chatInfo.addMember(userid);
                    curUser.setBackgroundColor(getResources().getColor(R.color.input_background));
                }
                else
                {
                    chatInfo.removeMember(userid);
                    curUser.setBackgroundColor(getResources().getColor(R.color.white));
                }

            });
    }
}