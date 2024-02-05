package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.SentMsgBinding;

public class SentMessageView extends MessageView {
    private SentMsgBinding B;
    public SentMessageView(Context context, MsgInfo msgInfo)
    {
        super(context);
        init(msgInfo);
    }
    public SentMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void init(MsgInfo msgInfo)
    {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.sent_msg,
                this,
                true
        );
        B.setMsgInfo(msgInfo);
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