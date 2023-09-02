package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


//import org.apache.http.client.HttpClient;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.api.MsgAPIClient;
import mikhail.shell.gleamy.dao.MessageDAO;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.models.MsgInfo;
//import java.net.http.HttpClient
public class ChatActivity extends AppCompatActivity {

    //private HttpClient httpClient;
    private MessageDAO msgDAO;

    private long chatid, userid;
    private Map<Long,MsgInfo> msgInfos;



    private Map<Long, Message> msgs;

    private LinearLayout chatContent;



    private EditText chatTextArea;
    private ImageView sendBtn;

    private Message targetMessage;

    private View.OnClickListener sendListener, updListener;
    private MsgAPIClient msgClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        getBundle();

        msgDAO = MessageDAO.getInstance();

        chatContent = findViewById(R.id.chatContent);

        msgs = new HashMap<>();
        msgClient = MsgAPIClient.getClient();
        msgClient.addActivity("ChatActivity", this);

        msgClient.getChatMessages(chatid);

        initSendListener();
        initUpdateListener();


        chatTextArea = findViewById(R.id.chatTextArea);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(sendListener);

    }
    @Override
    protected void onStart()
    {
        super.onStart();

    }
    private void getBundle()
    {
        Bundle info = getIntent().getExtras();
        userid = info.getLong("userid");
        chatid = info.getLong("chatid");
    }
    public void viewAllMessages(Map<Long, MsgInfo> msgInfos)
    {
        Message msg;
        int i = 0;
        for (Map.Entry<Long,MsgInfo> info : msgInfos.entrySet())
        {
            msgs.put(info.getKey(), createMessage(info.getValue()));
            displayMessage(getMessage(info.getKey()));
        }
    }

    public Message getMessage(long msgid)
    {
        return msgs.get(msgid);
    }
    public void sendMessage(String text)
    {
        MsgInfo msgInfo = new MsgInfo(userid,chatid,0,true,text, new Date());
        msgClient.sendMessage(msgInfo);


    }
    public Message createMessage(MsgInfo msgInfo)
    {
        int viewId;
        msgInfo.isMine = msgInfo.userid == userid;
        if (msgInfo.isMine)
            viewId = R.layout.sent_msg;
        else
            viewId = R.layout.received_msg;
        Message msg = (Message) LayoutInflater.from(this).inflate(viewId, null);
        msg.init();
        msg.setInfo(msgInfo);
        if (msgInfo.isMine) {
            msg.setGravity(Gravity.RIGHT);
        }
        else
        {
            msg.setGravity(Gravity.LEFT);
        }

        registerForContextMenu(msg);
        return msg;
    }

    public void displayMessage(Message msg)
    {
        chatContent.addView(msg);
    }
    private void removeMessage(long msgid)
    {
        chatContent.removeView(msgs.get(msgid));
        msgs.remove(msgid);
        msgDAO.removeMessage(msgid);
    }
    private void startUpdatingMessage(Message msg)
    {
        chatTextArea.setText(msg.info.text);
        sendBtn.setOnClickListener(updListener);
    }
    private void updateMessage(MsgInfo msgInfo){
        chatTextArea.setText("");
        msgDAO.updateMessage(msgInfo);
        msgs.get(msgInfo.msgid).setInfo(msgInfo);
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {System.out.println(targetMessage + "item --------");
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
    }
    private void initSendListener()
    {
        sendListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = chatTextArea.getText().toString();
                if (!text.equals(""))
                {
                    sendMessage(text);

                }
            }
        };
    }
    private void initUpdateListener()
    {
        updListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = chatTextArea.getText().toString();
                MsgInfo info = targetMessage.info;
                info.text = text;
                msgDAO.updateMessage(info);
                updateMessage(info);
                sendBtn.setOnClickListener(sendListener);
            }
        };
    }
    public Map<Long, Message> getMsgs() {
        return msgs;
    }
    public EditText getChatTextArea() {
        return chatTextArea;
    }

}