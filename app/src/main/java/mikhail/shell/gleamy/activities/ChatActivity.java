package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.databinding.ChatActivityBinding;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.User;
import mikhail.shell.gleamy.views.DateView;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.views.MessageView;
import mikhail.shell.gleamy.views.ReceivedMessageView;
import mikhail.shell.gleamy.views.SentMessageView;
import mikhail.shell.gleamy.viewmodels.ChatViewModel;

public class ChatActivity extends AppCompatActivity {
    private ChatActivityBinding B;
    private Map<Long, MessageView> msgs;
    private Map<Long, byte[]> avatars;
    private ChatViewModel chatViewModel;
    private Message lastMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        B = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        getBundle();

        msgs = new TreeMap<>();
        avatars = new TreeMap<>();
        initChat();
        setSendListener();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    private void initChat()
    {
        chatViewModel.fetchAllMessages(this,
                msgsMap -> {

                    addAllMessages(new ArrayList<>(msgsMap.values()));
                    fetchAllAvatars();

                    chatViewModel.removeObservers(this);

                    chatViewModel.observeIncomingMessage(this,
                            (message) -> {
                                Message msg = message.getModel();
                                if (msg!=null)
                                    if (!msgs.containsKey(msg.getMsgid()))
                                    {
                                        addMessage(msg);
                                        scrollToBottom();
                                    }
                            }
                    );
                }
        );
        //chatViewModel.fetchAllChatMembers();
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
            messages.forEach(this::addMessage);
            scrollToBottom();
        }
    }
    private void addMessage(Message msg)
    {
        MessageView messageView = createMessage(msg);
        displayMessage(messageView);
        msgs.put(msg.getMsgid(), messageView);
        lastMessage = msg;
        if (messageView instanceof ReceivedMessageView && avatars.containsKey(messageView.getMsgInfo().getUserid()))
            ((ReceivedMessageView) messageView).setAvatar(avatars.get(messageView.getMsgInfo().getUserid()));
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
        if (msgs.isEmpty())
            clear();
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
            if (lastMessage == null)
                return true;
            else
                return !date.equals(lastMessage.getDateTime().toLocalDate());
        }
        else
            return false;
    }
    private void manageNewDateTime(LocalDateTime dateTime)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && dateTime != null) {
            LocalDate date = dateTime.toLocalDate();
            if (needsNewDate(date)) {
                boolean withYear = true;
                if (lastMessage != null)
                    withYear = date.getYear() > lastMessage.getDateTime().getYear();
                addDateView(date, withYear);
            }
        }
    }
    private void scrollToBottom()
    {
        B.chatScrollView.post(() -> B.chatScrollView.fullScroll(View.FOCUS_DOWN));
    }
    private void fetchAllAvatars()
    {
        chatViewModel.fetchAllAvatarsByChatId(this, incomingAva -> {
            if (incomingAva != null)
                msgs.values().stream().filter(
                        msgView ->
                        {
                            long userid = msgView.getMsgInfo().getUserid();
                            long ownUserid = getSharedPreferences("authdetails", MODE_PRIVATE).getLong("userid", 0);
                            List<User> userList = chatViewModel.getChatData().getValue().getUsers();
                            Optional<User> optionalUser = userList.stream().filter(curUser -> curUser.getId() == userid).findAny();
                            if (optionalUser.isPresent())
                            {
                                String avatar = optionalUser.get().getAvatar();
                                return avatar != null ? avatar.equals(incomingAva.getDetails().get("filename")) : false;
                            }
                            else
                                return false;
                        }
                ).forEachOrdered(
                        msgView -> {
                            avatars.put(msgView.getMsgInfo().getUserid(), incomingAva.getModel());
                            if (msgView instanceof ReceivedMessageView)
                                ((ReceivedMessageView) msgView).setAvatar(incomingAva.getModel());
                        }
                );
        });
    }
}