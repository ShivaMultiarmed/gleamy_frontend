package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ReceivedMsgBinding;

public class ReceivedMessageView extends MessageView {
    private ReceivedMsgBinding B;
    public ReceivedMessageView(Context context, MsgInfo msgInfo)
    {
        super(context);
        init(msgInfo);
    }
    public ReceivedMessageView(Context context, AttributeSet attrs) {
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
        //correctLayoutParams();
    }
    private void correctLayoutParams()
    {
        LayoutParams layoutParams = (LayoutParams) ((LinearLayout)this).getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;
        setLayoutParams(layoutParams);
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
