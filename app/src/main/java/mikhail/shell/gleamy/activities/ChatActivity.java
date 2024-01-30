package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatActivityBinding;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.DateView;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.ReceivedMessage;
import mikhail.shell.gleamy.models.SentMessage;
import mikhail.shell.gleamy.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    private ChatActivityBinding B;
    private Map<Long, Message> msgs;
    private ChatViewModel chatViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        B = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        getBundle();

        msgs = new TreeMap<>();
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        initChat();

        setSendListener();
    }
    private void initChat()
    {
        chatViewModel.fetchAllMessages();
        LifecycleOwner subscriber = this;
        chatViewModel.observeMessages(subscriber,
                (initialMap) -> {
                    addAllMessages(new ArrayList<>(initialMap.values()));
                    chatViewModel.removeObservers(subscriber);
                    chatViewModel.observeMessages(subscriber,
                            (map) -> {
                                MsgInfo msg = chatViewModel.getLastMessage();
                                addMessage(msg);
                            }
                    );
                }
        );
        chatViewModel.fetchAllChatMembers();
    }
    private void getBundle()
    {
        Bundle b = getIntent().getExtras();
        ChatInfo chat = (ChatInfo) b.getSerializable("chatInfo");
        chatViewModel = new ViewModelProvider(this, new ChatViewModel.ChatViewModelFactory(chat)).get(ChatViewModel.class);
        B.setChatInfo(chat);
    }
    private void addAllMessages(List<MsgInfo> msgInfos)
    {
        if (!msgInfos.isEmpty())
        {
            clear();
            msgInfos.stream().forEach(msg -> addMessage(msg));
            scrollToBottom();
        }
    }
    private void addMessage(MsgInfo msg)
    {
        Message message = createMessage(msg);
        msgs.put(msg.getMsgid(), message);
        displayMessage(message);
    }

    private Message createMessage(MsgInfo msgInfo)
    {
        long userid = GleamyApp.getApp().getUser().getId();
        msgInfo.isMine = msgInfo.userid == userid;
        Message msg = msgInfo.isMine ? new SentMessage(this, msgInfo) : new ReceivedMessage(this, msgInfo);
        return msg;
    }

    private void displayMessage(Message msg)
    {
        LocalDateTime newDateTime = msg.getMsgInfo().getDateTime();
        manageNewDateTime(newDateTime);

        B.chatContent.addView(msg);
    }
    private void setSendListener()
    {
        B.sendBtn.setOnClickListener(
                view -> {
                    String text = B.chatTextArea.getText().toString();
                    if (!text.isEmpty())
                        chatViewModel.sendMessage(text);
                    B.chatTextArea.setText("");
                }
        );
    }
    private void clear()
    {
        B.chatContent.removeAllViews();
    }
    private void addDateView(LocalDate date, boolean withYear)
    {
        DateView dateView = new DateView(this,date, withYear);
        B.chatContent.addView(dateView);
    }
    private boolean needsNewDate(LocalDate date)
    {
        return !date.equals(chatViewModel.getLastMsgDate()) || chatViewModel.getLastMsgDate() == null;
    }
    private void manageNewDateTime(LocalDateTime dateTime)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && dateTime != null) {
            LocalDate date = dateTime.toLocalDate();
            if (needsNewDate(date)) {
                boolean withYear;
                if (chatViewModel.getLastMsgDate() != null)
                    withYear = date.getYear() > chatViewModel.getLastMsgDate().getYear();
                else
                    withYear = true;
                addDateView(date, withYear);
            }
        }
    }
    private void scrollToBottom()
    {
        B.chatScrollView.fullScroll(View.FOCUS_DOWN);
    }
}