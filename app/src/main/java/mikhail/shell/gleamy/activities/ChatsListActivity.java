package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatsListActivityBinding;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.ChatView;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.viewmodels.ChatsListViewModel;
import mikhail.shell.gleamy.viewmodels.UserViewModel;

public class ChatsListActivity extends AppCompatActivity {
    private ChatsListActivityBinding B;
    private static final int CREATE_CHAT_INTENT = 1;
    private Map<Long,ChatView> chats;
    private OpenChatListener openChatListener;
    private ChatsListViewModel chatsListViewModel;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ChatsListActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        retrieveBundle();
        initViewModels();
        initChatsMap();
        initViews();

        chatsListViewModel.fetchAllChatsFromREST();
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

        chatsListViewModel.getChatsLiveData().observe(this, chats -> displayAllChats(chats));
        chatsListViewModel.getLatestChatLiveData().observe(this,
                (latestChat) -> {
                    addChat(latestChat);

                    long chatid = latestChat.getId();
                    MsgInfo msg = latestChat.getLast();
                    elevateChat(chatid, msg);
                }
        );
        chatsListViewModel.fetchAllChatsFromREST();

    }
    private void initChatsMap()
    {
        chats = new LinkedHashMap<>();
    }
    private void initViews()
    {
        B.addChatBtn.setOnClickListener(e-> createChat());
        openChatListener = new OpenChatListener();
    }
    private void displayAllChats(Map<Long, ChatInfo> chatInfos)
    {
        if (!chatInfos.isEmpty())
            for (ChatInfo chatInfo : chatInfos.values())
                addChat(chatInfo);
    }
    private ChatView createChatView(ChatInfo info)
    {
        ChatView chat = new ChatView(this, info);
        chat.setOnClickListener(openChatListener);
        return chat;
    }
    private void displayChat(ChatView chat)
    {
        B.chatsListContent.addView(chat, 0);
    }
    public void addChat(ChatInfo chatInfo)
    {
        if (chats.isEmpty())
            clear();
        ChatView chatView = createChatView(chatInfo);
        chats.put(chatInfo.getId(), chatView);
        displayChat(chatView);
    }
    private void createChat()
    {
        Intent chatCreation = new Intent(this, CreateChatActivity.class);
        Bundle b = new Bundle();
        long userid = GleamyApp.getApp().getUser().getId();
        b.putLong("userid", userid);
        chatCreation.putExtras(b);
        startActivityForResult(chatCreation, CREATE_CHAT_INTENT);
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
                    ChatInfo chatInfo = (ChatInfo) b.getSerializable("chatinfo");
                    addChat(chatInfo);
                }
                default -> {}
            }
    }

    private void openChat(ChatInfo chat)
    {
        Intent openChat = new Intent(this, ChatActivity.class);
        Bundle info = new Bundle();
        long userid = GleamyApp.getApp().getUser().getId();
        info.putLong("userid", userid);
        info.putSerializable("chatInfo", chat);
        openChat.putExtras(info);
        startActivity(openChat);
    }

    public void clear()
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
    public void elevateChat(long chatid, MsgInfo last)
    {
        B.chatsListContent.removeView(getChatView(chatid));
        B.chatsListContent.addView(getChatView(chatid), 0);
    }
    public ChatView getChatView(long chatid)
    {
        return chats.get(chatid);
    }
    public ChatInfo getChatInfo(long chatid)
    {
        return getChatView(chatid).getInfo();
    }
    public ChatInfo getLatestChat()
    {
        return null;
    }
}