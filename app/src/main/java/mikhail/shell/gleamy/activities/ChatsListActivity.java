package mikhail.shell.gleamy.activities;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatsListActivityBinding;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.views.ChatView;
import mikhail.shell.gleamy.viewmodels.ChatsListViewModel;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class ChatsListActivity extends AppCompatActivity {
    private ChatsListActivityBinding B;
    private static final int CREATE_CHAT_INTENT = 1;
    private Map<Long,ChatView> chats;
    private OpenChatListener openChatListener;
    private ChatsListViewModel chatsListViewModel;
    private UserViewModel userViewModel;
    private ActivityResultLauncher createChatLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ChatsListActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        retrieveBundle();
        initViewModels();
        chats = new LinkedHashMap<>();
        initViews();

        chatsListViewModel.fetchAllChatsFromREST();

        initCreateChatLauncher();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

    }
    private void retrieveBundle()
    {
        //Bundle b = getIntent().getExtras();
        //userid = b.getLong("userid");
    }
    private void initViewModels()
    {
        chatsListViewModel = ViewModelProviders.of(this).get(ChatsListViewModel.class);
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        chatsListViewModel.getChatsLiveData().observe(this,
                chatMap ->
                {
                    /*if (!chats.isEmpty())
                        removeEmptyMessage();*/

                    addAllChats(chatMap);

                    chatsListViewModel.getChatsLiveData().removeObservers(this);

                    chatsListViewModel.getChatsLiveData().observeForever(updatedChats -> {
                        Chat lastChat = chatsListViewModel.getLastChat();
                        if (lastChat != null)
                        {
                            if (!chats.containsKey(lastChat.getId()))
                                addChat(lastChat);
                            elevateChat(lastChat);
                        }
                    });
                }
        );

    }
    private void initViews()
    {
        B.addChatBtn.setOnClickListener(v-> createChat());
        B.logoutbtn.setOnClickListener(v -> logout());
        openChatListener = new OpenChatListener();
    }
    private void addAllChats(Map<Long, Chat> chats)
    {
        if (!chats.isEmpty())
            chats.values().forEach(this::addChat);
    }
    private ChatView createChatView(Chat chat)
    {
        ChatView chatView = new ChatView(this, chat);
        chatView.setOnClickListener(openChatListener);
        return chatView;
    }
    private void displayChat(ChatView chat)
    {
        B.chatsListContent.addView(chat, 0);
    }
    public void addChat(Chat chat)
    {
        ChatView chatView = createChatView(chat);
        chats.put(chat.getId(), chatView);
        displayChat(chatView);
    }
    private void createChat()
    {
        long userid = GleamyApp.getApp().getUser().getId();
        createChatLauncher.launch(userid);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            switch (requestCode)
            {
                case CREATE_CHAT_INTENT -> {
                    Bundle b = data.getExtras();
                    Chat chat = (Chat) b.getSerializable("chatinfo");
                    addChat(chat);
                }
                default -> {}
            }
    }

    private void openChat(Chat chat)
    {
        Intent openChat = new Intent(this, ChatActivity.class);
        Bundle info = new Bundle();
        long userid = GleamyApp.getApp().getUser().getId();
        info.putLong("userid", userid);
        info.putSerializable("chatInfo", chat);
        openChat.putExtras(info);
        startActivity(openChat);
    }

    public void removeEmptyMessage()
    {
        B.chatsListContent.removeAllViews();
    }
    public boolean isEmpty()
    {
        return chats.isEmpty();
    }

    private class OpenChatListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            ChatView chatView = (ChatView) view;
            openChat(chatView.getInfo());
        }
    }
    public void elevateChat(Chat chat)
    {
        long chatid = chat.getId();
        ChatView chatView = getChatView(chatid);
        chatView.setChat(chat);
        B.chatsListContent.removeView(chatView);
        B.chatsListContent.addView(chatView, 0);
    }
    public ChatView getChatView(long chatid)
    {
        return chats.get(chatid);
    }
    public Chat getChat(long chatid)
    {
        return getChatView(chatid).getInfo();
    }
    public Chat getLatestChat()
    {
        return null;
    }
    private void initCreateChatLauncher()
    {
        ActivityResultContract<Long, Void> contract = new ActivityResultContract<>() {
            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Long userid) {
                Intent chatCreation = new Intent(context, CreateChatActivity.class);
                chatCreation.putExtra("userid", userid);
                return chatCreation;
            }

            @Override
            public Void parseResult(int i, @Nullable Intent intent) {
                return null;
            }
        };
        ActivityResultCallback<Void> callback = (aVoid) -> {};
        createChatLauncher = registerForActivityResult(contract, callback);
    }
    private void logout()
    {
        chatsListViewModel.logout();
        SharedPreferences.Editor editor = getSharedPreferences("authdetails", MODE_PRIVATE).edit();
        editor.remove("userid");
        editor.apply();
        goToAuth();
    }
    private void goToAuth()
    {
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
        finish();
    }
}