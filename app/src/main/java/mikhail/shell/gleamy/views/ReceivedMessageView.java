package mikhail.shell.gleamy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.activities.ChatActivity;
import mikhail.shell.gleamy.activities.UserActivity;
import mikhail.shell.gleamy.databinding.ReceivedMsgBinding;
import mikhail.shell.gleamy.models.Message;

public class ReceivedMessageView extends MessageView {
    private ReceivedMsgBinding B;
    public ReceivedMessageView(Context context, Message message)
    {
        super(context, message);
    }
    public ReceivedMessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void init(Message message)
    {
        B = DataBindingUtil.inflate(
                LayoutInflater.from(getContext()),
                R.layout.received_msg,
                this,
                true
        );
        setMsgInfo(message);
        B.userAva.setOnClickListener(view -> goToUserProfile());
    }

    @Override
    public void setMsgInfo(Message message) {
        B.setMessage(message);
    }
    @Override
    public Message getMsgInfo() {
        return B.getMessage();
    }
    protected void goToUserProfile() {
        Intent intent = new Intent(getContext(), UserActivity.class);
        ChatActivity chatActivity = (ChatActivity) getContext();
        intent.putExtra("userid", B.getMessage().getUserid());
        chatActivity.startActivity(intent);
    }
    public void setAvatar(byte[] imageBytes)
    {
        B.userAva.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length));
    }
}
