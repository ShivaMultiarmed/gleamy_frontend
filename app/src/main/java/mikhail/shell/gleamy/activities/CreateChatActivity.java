package mikhail.shell.gleamy.activities;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.views.UserView;
import mikhail.shell.gleamy.viewmodels.CreateChatViewModel;
import mikhail.shell.gleamy.databinding.CreateChatActivityBinding;

public class CreateChatActivity extends AppCompatActivity {
    private CreateChatActivityBinding B;
    private CreateChatViewModel viewModel;
    private Map<Long, UserView> users;
    private View.OnClickListener chooseListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = CreateChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        User user = GleamyApp.getApp().getUser();
        viewModel = new ViewModelProvider(this, new CreateChatViewModel.Factory(user)).get(CreateChatViewModel.class);

        initListeners();
        initObservers();
    }

    private void initListeners()
    {
        B.createChatBtn.setOnClickListener(e->{
            String input = B.chatTitleInput.getText().toString();
            viewModel.getChat().setTitle(input);
            viewModel.addChat();
        });
        B.searchUsersBtn.setOnClickListener(view->{
            String input = B.searchUsersInput.getText().toString();
            viewModel.getUsersByLogin(input);
        });
    }
    private void initObservers()
    {
        viewModel.observeChatMembers(this,
            members ->
            {
                if (!members.isEmpty())
                {
                    createUsers(members);
                    displayUsers();
                    setChooseListener();
                }
                else
                {
                    clear();
                    Toast.makeText(this,"Пользователи не найдены.", Toast.LENGTH_SHORT);
                }

            }
        );
        viewModel.observeStatus(this,
                status -> {
                    if (status.equals("OK"))
                        goToChatsList();
                    else
                        Toast.makeText(this,"Не удалось создать чат.", Toast.LENGTH_SHORT);
                }
        );
    }
    private void createUsers(Map<Long, User> infos)
    {
        users = new HashMap<>();
        for (User info : infos.values())
            users.put(info.getId(),createUser(info));
    }
    private void displayUsers()
    {
        clear();
        users.values().forEach(this::displayUser);
    }
    private UserView createUser(User info)
    {
        UserView userView = (UserView)  LayoutInflater.from(this).inflate(R.layout.user_view, null);
        userView.init();
        userView.setUser(info);
        setActive(userView);
        return userView;
    }
    private void displayUser(UserView userView)
    {
        B.addUsersToChat.addView(userView);
    }
    private void setActive(UserView userView)
    {
        Chat chat = viewModel.getChat();
        long userid = userView.getUser().getId();
        int color = chat.hasMember(userid) ? R.color.input_background : R.color.white;
        userView.setBackgroundColor(getResources().getColor(color));
    }
    private void clear()
    {
        B.addUsersToChat.removeAllViews();
    }
    private void setChooseListener()
    {
        Chat chat = viewModel.getChat();
        chooseListener = view->{
            UserView userView = (UserView) view;
            long userid = userView.getUser().getId();
            if (!chat.hasMember(userid))
                chat.addMember(userid);
            else
                chat.removeMember(userid);
            setActive(userView);
        };
        users.values().forEach(userView -> userView.setOnClickListener(chooseListener));
    }
    private void goToChatsList()
    {
        setResult(RESULT_OK, getIntent());
        finish();
    }
}