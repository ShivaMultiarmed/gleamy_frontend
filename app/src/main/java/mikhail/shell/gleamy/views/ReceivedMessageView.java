package mikhail.shell.gleamy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.databinding.DataBindingUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

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
        //Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        Bitmap bitmap = getCircleBitmap(imageBytes);
        B.userAva.setImageBitmap(bitmap);
    }
    private Bitmap getCircleBitmap(byte[] imageBytes) {

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circleBitmap);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);

        int radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        paint.setXfermode(new android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return circleBitmap;
    }
}
