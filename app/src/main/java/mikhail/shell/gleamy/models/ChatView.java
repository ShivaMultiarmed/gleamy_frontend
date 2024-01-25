package mikhail.shell.gleamy.models;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import lombok.Getter;
import lombok.Setter;
import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ChatViewBinding;

@Getter
@Setter
public class ChatView extends LinearLayout {

    private ChatViewBinding B;

    public ChatView(Context context, ChatInfo chatInfo) {
        super(context);
        init(chatInfo);
    }
    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChatInfo getInfo() {
        return B.getChatInfo();
    }

    private void init(ChatInfo chatInfo)
    {
        LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
        B = DataBindingUtil.inflate(inflater, R.layout.chat_view, this, true);
        B.setChatInfo(chatInfo);
    }

    public MsgInfo getLastMsg()
    {
        return B.getChatInfo().getLast();
    }
}