package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import lombok.Getter;
import lombok.Setter;
import mikhail.shell.gleamy.R;

@Getter
@Setter
public class ChatView extends LinearLayout {
    private ChatInfo info;
    private TextView chatTitle, lastMsg;
    private ImageView chatAva;

    public ChatView(Context context) {
        super(context);
    }
    public ChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ChatInfo getInfo() {
        return info;
    }

    public void setInfo(ChatInfo info) {
        this.info = info;
        chatTitle.setText(info.getTitle());
        lastMsg.setText(info.getLast().getText());
    }
    public void init()
    {
        chatTitle = findViewById(R.id.chatTitle);
        chatAva = findViewById(R.id.chatAva);
        lastMsg = findViewById(R.id.lastMsg);
    }
}
