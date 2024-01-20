package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ReceivedMsgBinding;

public class ReceivedMessage extends Message{
    private ReceivedMsgBinding B;
    public ReceivedMessage(Context context, MsgInfo msgInfo)
    {
        super(context);
        init(msgInfo);
    }
    public ReceivedMessage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void init(MsgInfo msgInfo)
    {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.received_msg,
                this,
                true
        );
        setMsgInfo(msgInfo);
    }
    @Override
    public void setMsgInfo(MsgInfo msgInfo) {
        B.setMsgInfo(msgInfo);
    }
    @Override
    public MsgInfo getMsgInfo() {
        return B.getMsgInfo();
    }
}
