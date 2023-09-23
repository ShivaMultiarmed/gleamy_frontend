package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.ChatAPIClient;
import mikhail.shell.gleamy.api.ChatApi;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.ChatView;
import mikhail.shell.gleamy.models.MsgInfo;

public class ChatsList extends AppCompatActivity {
    private Context thisActivity;
    private static final int CREATE_CHAT_INTENT = 1;
    private long userid;
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
        AbstractAPI.addActivity("ChatsList", this);
        chats = new LinkedHashMap<>();

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
        if (!chatInfos.isEmpty())
            for (ChatInfo chatInfo : chatInfos.values())
            {
                addChat(chatInfo);
            }
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
                case CREATE_CHAT_INTENT:
                    Bundle b = data.getExtras();
                    ChatInfo chatInfo = (ChatInfo) b.getSerializable("chatinfo");
                    addChat(chatInfo);
                    break;
                default:
                    break;
            }
    }

    private void openChat(ChatInfo chat)
    {
        Intent openChat = new Intent(this, ChatActivity.class);
        Bundle info = new Bundle();
        info.putLong("userid", userid);
        info.putSerializable("chatInfo", chat);
        openChat.putExtras(info);
        startActivity(openChat);
    }

    public void clear()
    {
        chatsList.removeAllViews();
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
            long chatid = chatView.getInfo().getId();
            openChat(chats.get(chatid).getInfo());
        }
    }
    public void elevateChat(long chatid, MsgInfo last)
    {
        getChatInfo(chatid).setLast(last);
        getChatView(chatid).getLastMsg().setText(last.getText());
        chatsList.removeView(getChatView(chatid));
        chatsList.addView(getChatView(chatid), 0);
    }
    public ChatView getChatView(long chatid)
    {
        return chats.get(chatid);
    }
    public ChatInfo getChatInfo(long chatid)
    {
        return getChatView(chatid).getInfo();
    }
}