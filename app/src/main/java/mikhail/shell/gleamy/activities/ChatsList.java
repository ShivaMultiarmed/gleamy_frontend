package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.ChatAPIClient;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.ChatView;

public class ChatsList extends AppCompatActivity {

    private long userid;
    private Map<Long,ChatInfo> chatInfos;
    private Map<Long,ChatView> chats;
    private ChatAPIClient chatsClient;
    private LinearLayout chatsList;
    private ChatApi chatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_list);
        retrieveBundle();
        init();
        initViews();
        chatsClient.getAllChats(userid);
    }
    private void retrieveBundle()
    {
        Bundle b = getIntent().getExtras();
        userid = b.getLong("userid");
    }
    private void init()
    {

        chatsClient  = ChatAPIClient.getClient();
        chatsClient.addActivity("ChatsList", this);
        chats = new HashMap<>();

    }
    private void initViews()
    {
        chatsList = findViewById(R.id.chatsListContent);
    }
    public void displayAllChats(Map<Long, ChatInfo> chatInfos)
    {
        for (ChatInfo chatInfo : chatInfos.values())
        {
            ChatView chatView = createChatView(chatInfo);
            chats.put(chatInfo.getId(), chatView);
            displayChat(chatView);
        }
    }
    private ChatView createChatView(ChatInfo info)
    {
        ChatView chat= (ChatView) LayoutInflater.from(this).inflate(R.layout.chat_view, null);
        chat.init();
        chat.setInfo(info);
        return chat;
    }
    private void displayChat(ChatView chat)
    {
        chatsList.addView(chat);
    }
}