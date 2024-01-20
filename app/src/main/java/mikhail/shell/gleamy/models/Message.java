package mikhail.shell.gleamy.models;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewbinding.ViewBinding;

import java.time.LocalDateTime;
import java.time.LocalTime;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ReceivedMsgBinding;


public abstract class Message extends LinearLayout {
    public Message(Context context)
    {
        super(context);
    }
    public Message(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    abstract protected void init(MsgInfo msgInfo);
    abstract public void setMsgInfo(MsgInfo msgInfo);
    abstract public MsgInfo getMsgInfo();
}
