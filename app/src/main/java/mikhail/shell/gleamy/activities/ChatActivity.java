package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.AbstractAPI;
import mikhail.shell.gleamy.api.MsgAPIClient;
import mikhail.shell.gleamy.dao.MessageDAO;
import mikhail.shell.gleamy.databinding.ChatActivityBinding;
import mikhail.shell.gleamy.models.ChatInfo;
import mikhail.shell.gleamy.models.DateView;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.MsgInfo;
import mikhail.shell.gleamy.models.ReceivedMessage;
import mikhail.shell.gleamy.models.SentMessage;

public class ChatActivity extends AppCompatActivity {
    private ChatActivityBinding B;
    private MessageDAO msgDAO;
    private ChatInfo chatInfo;
    private long chatid, userid;
    private Map<Long,MsgInfo> msgInfos;
    private Map<Long, Message> msgs;
    private Message targetMessage;
    private View.OnClickListener sendListener, updListener;
    private MsgAPIClient msgClient;
    private LocalDate lastMsgDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        B = ChatActivityBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        getBundle();

        msgDAO = MessageDAO.getInstance();

        msgs = new TreeMap<>();
        msgClient = MsgAPIClient.getClient();
        AbstractAPI.addActivity("ChatActivity", this);

        msgClient.getChatMessages(chatid);

        initSendListener();
        //initUpdateListener();

        B.sendBtn.setOnClickListener(sendListener);
        B.setChatInfo(chatInfo);


    }
    @Override
    protected void onStart()
    {
        super.onStart();
    }
    private void getBundle()
    {
        Bundle b = getIntent().getExtras();
        chatInfo = (ChatInfo) b.getSerializable("chatInfo");
        userid = b.getLong("userid", 0);
        chatid = chatInfo.getId();

    }
    public long getChatid() {
        return chatid;
    }

    public long getUserid() {
        return userid;
    }
    public void viewAllMessages(List<MsgInfo> msgInfos)
    {
        if (!msgInfos.isEmpty())
        {
            clear();
            for (MsgInfo info : msgInfos)
            {
                msgs.put(info.getMsgid(), createMessage(info));
                displayMessage(getMessage(info.getMsgid()));
            }
            scrollToBottom();
        }

    }
    public Message getMessage(long msgid)
    {
        return msgs.get(msgid);
    }
    public void sendMessage(String text)
    {
        B.chatTextArea.setText("");
        MsgInfo msgInfo = new MsgInfo(userid,chatid,0,true,text);
        msgClient.sendMessage(msgInfo);
    }
    public Message createMessage(MsgInfo msgInfo)
    {
        msgInfo.isMine = msgInfo.userid == userid;
        Message msg = msgInfo.isMine ? new SentMessage(this, msgInfo) : new ReceivedMessage(this, msgInfo);

        registerForContextMenu(msg); // need to remove
        return msg;
    }

    public void displayMessage(Message msg)
    {
        LocalDateTime newDateTime = msg.getMsgInfo().getDateTime();
        manageNewDateTime(newDateTime);

        B.chatContent.addView(msg);
    }
    private void removeMessage(long msgid)
    {
        B.chatContent.removeView(msgs.get(msgid));
        msgs.remove(msgid);
        msgDAO.removeMessage(msgid);
    }
    private void startUpdatingMessage(Message msg)
    {
        B.chatTextArea.setText(msg.getMsgInfo().getText());
        B.sendBtn.setOnClickListener(updListener);
    }
    private void updateMessage(MsgInfo msgInfo){
        B.chatTextArea.setText("");
        msgDAO.updateMessage(msgInfo);
        msgs.get(msgInfo.msgid).setMsgInfo(msgInfo);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo info)
    {
        super.onCreateContextMenu(menu, view, info);
        MenuInflater inflater  = new MenuInflater(this);
        inflater.inflate(R.menu.contextmenu, menu);
        targetMessage = (Message) view;
        System.out.println("update has started");
        startUpdatingMessage(targetMessage);
        //removeMessage(targetMessage.info.msgid);
    }
    /*@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        System.out.println(targetMessage + "item --------");
        switch (item.getItemId())
        {
            case R.id.edit:
                updateMessage(targetMessage.info);
                break;
            case R.id.remove:
                System.out.println(targetMessage + "removing --------");
                removeMessage(targetMessage.info.msgid);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/
    private void initSendListener()
    {
        sendListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = B.chatTextArea.getText().toString();
                if (!text.equals(""))
                {
                    sendMessage(text);

                }
            }
        };
    }
    /*private void initUpdateListener()
    {
        updListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = B.chatTextArea.getText().toString();
                MsgInfo info = targetMessage.info;
                info.text = text;
                msgDAO.updateMessage(info);
                updateMessage(info);
                B.sendBtn.setOnClickListener(sendListener);
            }
        };
    }*/
    public Map<Long, Message> getMsgs() {
        return msgs;
    }
    private LocalDate getLastMsgDate()
    {
        return lastMsgDate;
    }
    private void updateLastMsgDate(LocalDate date)
    {
        lastMsgDate = date;
    }
    public EditText getChatTextArea() {
        return B.chatTextArea;
    }
    public void clear()
    {
        B.chatContent.removeAllViews();
    }
    public void addDateView(LocalDate date, boolean withYear)
    {
        DateView dateView = new DateView(this,date, withYear);
        B.chatContent.addView(dateView);
    }
    private boolean needsNewDate(LocalDate date)
    {
        return !date.equals(getLastMsgDate()) || lastMsgDate == null;
    }
    private void manageNewDateTime(LocalDateTime dateTime)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && dateTime != null) {
            LocalDate date = dateTime.toLocalDate();
            if (needsNewDate(date)) {
                boolean withYear;
                if (lastMsgDate != null)
                    withYear = date.getYear() > lastMsgDate.getYear();
                else
                    withYear = true;
                updateLastMsgDate(date);
                addDateView(date, withYear);
            }
        }
    }
    private void scrollToBottom()
    {
        B.chatScrollView.fullScroll(View.FOCUS_DOWN);
    }
}