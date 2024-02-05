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
import mikhail.shell.gleamy.views.UserView;

public class ChatInfoActivity extends AppCompatActivity {
    private ChatInfoActivityBinding B;
    private long userid, chatid;
    private Map<Long, UserView> users;
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

    public void addAllUsers(List<User> users)
    {
        for (User user : users)
            addUser(user);
    }
    public void addUser(User user)
    {
        users.put(user.getId(), createUser(user));
    }
    private UserView createUser(User info)
    {
        UserView userView = (UserView)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        userView.init();
        userView.setUser(info);
        return userView;
    }
    public void displayAllUsers(Map<Long, UserView> users)
    {
        for (UserView userView : users.values())
            displayUser(userView);
    }
    public void displayUser(UserView userView)
    {
        B.chatInfoContent.addView(userView);
    }
}