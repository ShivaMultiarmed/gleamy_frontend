package mikhail.shell.gleamy.models;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalTime;

import mikhail.shell.gleamy.R;


public class Message extends LinearLayout {
    public MsgInfo info;
    private TextView text, publisher, dateTime;

    public Message(Context context) {
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

    public void init() {
        publisher = findViewById(R.id.msgPublisher);
        dateTime = findViewById(R.id.msgDateTime);
        text = findViewById(R.id.msgText);
    }

    public void setInfo(MsgInfo info) {
        this.info = info;
        text.setText(info.text);
        if (publisher != null && info.login != null)
            publisher.setText(info.login);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && info.dateTime != null)
            dateTime.setText(generateTimeString(info.getDateTime().toLocalTime()));
    }
    public String generateTimeString(LocalTime time)
    {
        StringBuilder builder = new StringBuilder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (time.getHour() < 10)
                builder.append(0);
            builder.append(time.getHour());
            builder.append(":");
            if (time.getMinute() < 10)
                builder.append(0);
            builder.append(time.getMinute());
        }
        return builder.toString();
    }

}
