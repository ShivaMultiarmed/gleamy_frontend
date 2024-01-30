package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ChatInfoActivityBinding;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;

public class ChatInfoActivity extends AppCompatActivity {
    private ChatInfoActivityBinding B;
    private long userid, chatid;
    private Map<Long, User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ChatInfoActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());
        initVars();
        initHttp();
    }
    private void initVars()
    {
        users = new HashMap<>();
    }
    private void getBundle()
    {
        Bundle b = getIntent().getExtras();
        userid = b.getLong("userid");
        chatid = b.getLong("chatid");
    }
    private void initHttp()
    {

    }

    public void addAllUsers(List<UserInfo> users)
    {
        for (UserInfo user : users)
            addUser(user);
    }
    public void addUser(UserInfo user)
    {
        users.put(user.getId(), createUser(user));
    }
    private User createUser(UserInfo info)
    {
        User user = (User)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        user.init();
        user.setUser(info);
        return user;
    }
    public void displayAllUsers(Map<Long, User> users)
    {
        for (User user : users.values())
            displayUser(user);
    }
    public void displayUser(User user)
    {
        B.chatInfoContent.addView(user);
    }
}