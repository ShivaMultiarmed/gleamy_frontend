package mikhail.shell.gleamy.models;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import lombok.Getter;
import lombok.Setter;
import mikhail.shell.gleamy.R;

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
        return B.getChatInfo();
    }

    private void init(Chat chat)
    {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        B = DataBindingUtil.inflate(inflater, R.layout.chat_view, this, true);
        B.setChatInfo(chat);
    }

    public Message getLastMsg()
    {
        return B.getChatInfo().getLast();
    }
}