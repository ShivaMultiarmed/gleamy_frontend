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

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatActivityBinding;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.views.DateView;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.views.MessageView;
import mikhail.shell.gleamy.views.ReceivedMessageView;
import mikhail.shell.gleamy.views.SentMessageView;
import mikhail.shell.gleamy.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    private ChatActivityBinding B;
    private Map<Long, MessageView> msgs;
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
                                Message msg = chatViewModel.getLastMessage();
                                if (msg!=null)
                                    if (!msgs.containsKey(msg.getMsgid()))
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
        Chat chat = (Chat) b.getSerializable("chatInfo");
        chatViewModel = new ViewModelProvider(this, new ChatViewModel.ChatViewModelFactory(chat)).get(ChatViewModel.class);
        B.setChat(chat);
    }
    private void addAllMessages(List<Message> messages)
    {
        if (!messages.isEmpty())
        {
            clear();
            messages.stream().forEach(msg -> addMessage(msg));
            scrollToBottom();
        }
    }
    private void addMessage(Message msg)
    {
        MessageView messageView = createMessage(msg);
        if (msgs.isEmpty())
            clear();
        msgs.put(msg.getMsgid(), messageView);
        displayMessage(messageView);
    }

    private MessageView createMessage(Message message)
    {
        long userid = GleamyApp.getApp().getUser().getId();
        message.isMine = message.userid == userid;
        MessageView msg = message.isMine ? new SentMessageView(this, message) : new ReceivedMessageView(this, message);
        return msg;
    }

    private void displayMessage(MessageView msg)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Message lastMsg = getSecondLastAddedMsg();
            if (lastMsg == null)
                return true;
            else
                return !date.equals(lastMsg.getDateTime().toLocalDate());
        }
        else
            return false;
    }
    private void manageNewDateTime(LocalDateTime dateTime)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && dateTime != null) {
            LocalDate date = dateTime.toLocalDate();
            if (needsNewDate(date)) {
                boolean withYear;
                if (getSecondLastAddedMsg() != null)
                    withYear = date.getYear() > getSecondLastAddedMsg().getDateTime().getYear();
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
    private Message getLastAddedMsg()
    {
        long msgid = msgs.keySet().stream().skip(msgs.size()-1).findFirst().get();
        return msgs.get(msgid).getMsgInfo();
    }
    private Message getSecondLastAddedMsg()
    {
        try{
            long msgid = msgs.keySet().stream().skip(msgs.size() - 2).findFirst().get();
            return msgs.get(msgid).getMsgInfo();
        }
        catch (IllegalArgumentException exception) {
            return null;
        }
    }
}