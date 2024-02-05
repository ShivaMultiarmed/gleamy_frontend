package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.Message;
import mikhail.shell.gleamy.databinding.SentMsgBinding;

public class SentMessageView extends MessageView {
    private SentMsgBinding B;
    public SentMessageView(Context context, Message message)
    {
        super(context);
        init(message);
    }
    public SentMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //correctLayoutParams();
    }
    @Override
    protected void init(Message message)
    {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.sent_msg,
                this,
                true
        );
        B.setMessage(message);
    }
    @Override
    public void setMsgInfo(Message message) {
        B.setMessage(message);
    }
    @Override
    public Message getMsgInfo() {
        return B.getMessage();
    }
    private void correctLayoutParams()
    {
        LayoutParams layoutParams = (LayoutParams) ((LinearLayout)this).getLayoutParams();
        layoutParams.gravity = Gravity.RIGHT;
        setLayoutParams(layoutParams);
    }
}
