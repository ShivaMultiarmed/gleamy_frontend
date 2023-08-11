package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
    private Context thisActivity;
    private static final int CREATE_CHAT_INTENT = 1;
    private long userid;
    private Map<Long,ChatInfo> chatInfos;
    private Map<Long,ChatView> chats;
    private ChatAPIClient chatsClient;
    private LinearLayout chatsList;
    private ChatApi chatApi;
    private ImageView addChatBtn;
    private OpenChatListener openChatListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thisActivity = this;
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
        addChatBtn = findViewById(R.id.addChatBtn);
        addChatBtn.setOnClickListener(e->{ createChat(); });
        openChatListener = new OpenChatListener();
    }
    public void displayAllChats(Map<Long, ChatInfo> chatInfos)
    {
        for (ChatInfo chatInfo : chatInfos.values())
            addChat(chatInfo);
    }
    private ChatView createChatView(ChatInfo info)
    {
        ChatView chat= (ChatView) LayoutInflater.from(this).inflate(R.layout.chat_view, null);
        chat.init();
        chat.setInfo(info);
        chat.setOnClickListener(openChatListener);
        return chat;
    }
    private void displayChat(ChatView chat)
    {
        chatsList.addView(chat, 0);
    }
    public void addChat(ChatInfo chatInfo)
    {
        ChatView chatView = createChatView(chatInfo);
        chats.put(chatInfo.getId(), chatView);
        displayChat(chatView);
    }
    private void createChat()
    {
        Intent chatCreation = new Intent(this, CreateChatActivity.class);
        Bundle b = new Bundle();
        b.putLong("userid", userid);
        chatCreation.putExtras(b);
        startActivityForResult(chatCreation, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case CREATE_CHAT_INTENT:
                Bundle b = data.getExtras();
                ChatInfo chatInfo = (ChatInfo) b.getSerializable("chatinfo");
                addChat(chatInfo);
                break;
            default:
                break;
        }
    }

    private void openChat(long chatid)
    {
        Intent openChat = new Intent(this, ChatActivity.class);
        Bundle info = new Bundle();
        info.putLong("userid", userid);
        info.putLong("chatid", chatid);
        openChat.putExtras(info);
        startActivity(openChat);
    }
    private class OpenChatListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view)
        {
            ChatView chatView = (ChatView) view;
            long chatid = chatView.getInfo().getId();
            openChat(chatid);
        }
    }
}