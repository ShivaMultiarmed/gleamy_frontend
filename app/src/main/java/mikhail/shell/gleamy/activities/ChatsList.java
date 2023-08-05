package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.ChatAPIClient;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.ChatView;

public class ChatsList extends AppCompatActivity {

    private long userid;
    private List<ChatInfo> chatInfos;
    private List<ChatView> chats;
    private ChatAPIClient chatsClient;
    private LinearLayout chatsList;
    private ChatApi chatApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chats_list);
        init();
        initViews();
        displayAllChats();
    }
    private void init()
    {
        chatsClient  = ChatAPIClient.getClient();
        chatInfos = new ArrayList<>();
        chatInfos = chatsClient.getAllChats(userid);
        chats = new ArrayList<>();
    }
    private void initViews()
    {
        chatsList = findViewById(R.id.chatsListContent);
    }
    private void displayAllChats()
    {
        for (ChatInfo chatInfo : chatInfos)
        {
            ChatView chatView = createChatView(chatInfo);
            chats.add(chatView);
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