package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.CreateChatActivityBinding;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.models.UserInfo;
import mikhail.shell.gleamy.viewmodels.CreateChatViewModel;

public class CreateChatActivity extends AppCompatActivity {
    private CreateChatActivityBinding B;
    private CreateChatViewModel viewModel;
    private Map<Long,User> users;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = CreateChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        UserInfo user = GleamyApp.getApp().getUser();
        viewModel = new ViewModelProvider(this, new CreateChatViewModel.Factory(user)).get(CreateChatViewModel.class);

        initListeners();
    }

    private void initListeners()
    {
        B.createChatBtn.setOnClickListener(e->{
            String input = B.chatTitleInput.getText().toString();
            viewModel.getChat().setTitle(input);
            viewModel.postChat();
        });
        B.searchUsersBtn.setOnClickListener(view->{
            String input = B.searchUsersInput.getText().toString();
            viewModel.getUsersByLogin(input);
        });
    }
    private void initObservers()
    {
        viewModel.observeChatMembers(this, members -> createUsers(members));
        viewModel.observeStatus(this,
                status -> {
                    if (status.equals("OK"))
                        goToChatsList();
                    else
                        Toast.makeText(this,"Не удалось создать чат.", Toast.LENGTH_SHORT);
                }
        );
    }
    private void createUsers(Map<Long,UserInfo> infos)
    {
        users = new HashMap<>();
        for (UserInfo info : infos.values())
            users.put(info.getId(),createUser(info));
    }
    private void displayUsers()
    {
        clear();
        for (User user : users.values())
             displayUser(user);
    }
    private User createUser(UserInfo info)
    {
        User user = (User)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        user.init();
        user.setUser(info);
        return user;
    }
    private void displayUser(User user)
    {
        B.addUsersToChat.addView(user);
    }
    private void clear()
    {
        B.addUsersToChat.removeAllViews();
    }
    private void setChooseListener()
    {
        ChatInfo chatInfo = viewModel.getChat();
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
    private void goToChatsList()
    {

    }
}