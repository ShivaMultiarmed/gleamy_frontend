package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;


public abstract class MessageView extends LinearLayout {
    public MessageView(Context context)
    {
        super(context);
    }
    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    abstract protected void init(MsgInfo msgInfo);
    abstract public void setMsgInfo(MsgInfo msgInfo);
    abstract public MsgInfo getMsgInfo();
}
