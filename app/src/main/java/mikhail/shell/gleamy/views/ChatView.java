package mikhail.shell.gleamy.views;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import lombok.Getter;
import lombok.Setter;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ChatViewBinding;
import mikhail.shell.gleamy.models.Chat;
import mikhail.shell.gleamy.models.Message;

@Getter
@Setter
public class ChatView extends LinearLayout {

    private ChatViewBinding B;

    public ChatView(Context context, Chat chat) {
        super(context);
        init(chat);
    }
    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Chat getInfo() {
        return B.getChat();
    }

    private void init(Chat chat)
    {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        B = DataBindingUtil.inflate(inflater, R.layout.chat_view, this, true);
        B.setChat(chat);
    }

    public Message getLastMsg()
    {
        return B.getChat().getLast();
    }
    public void setChat(Chat chat)
    {
        B.setChat(chat);
    }
    public Chat getChat()
    {
        return B.getChat();
    }
}