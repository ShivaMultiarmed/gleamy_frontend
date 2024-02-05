package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import mikhail.shell.gleamy.models.Message;


public abstract class MessageView extends LinearLayout {
    public MessageView(Context context)
    {
        super(context);
    }
    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    abstract protected void init(Message msgInfo);
    abstract public void setMsgInfo(Message msgInfo);
    abstract public Message getMsgInfo();
}
