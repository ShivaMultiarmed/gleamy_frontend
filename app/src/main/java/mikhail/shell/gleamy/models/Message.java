package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import mikhail.shell.gleamy.R;


public class Message extends LinearLayout {
    public MsgInfo info;
    private TextView text;
    public Message(Context context)
    {
        super(context);
        init();
    }

    public Message(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public Message(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public void init()
    {

        text = (TextView) findViewById(R.id.msgText);
    }
    public void setInfo(MsgInfo info)
    {
        this.info  = info;
        text.setText(info.text);
    }
}
