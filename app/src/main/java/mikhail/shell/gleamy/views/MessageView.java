package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import mikhail.shell.gleamy.models.Message;


public abstract class MessageView extends LinearLayout {
    public MessageView(Context context, Message message)
    {
        this(context, null, message);
    }
    public MessageView(Context context, AttributeSet attrs, Message message) {
        this(context, attrs, 0, message);
    }
    public MessageView(Context context, AttributeSet attrs, int defStyleAttr, Message message) {
        this(context, attrs, defStyleAttr, 0, message);
    }
    public MessageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, Message message) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(message);
    }
    abstract protected void init(Message msgInfo);
    abstract public void setMsgInfo(Message msgInfo);
    abstract public Message getMsgInfo();
}
