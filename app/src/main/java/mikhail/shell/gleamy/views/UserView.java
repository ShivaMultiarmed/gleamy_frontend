package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.UserViewBinding;
import mikhail.shell.gleamy.models.User;


public class UserView extends LinearLayout {
    private UserViewBinding B;
    public UserView(Context context, User user) {
        this(context, null, user);
    }
    public UserView(Context context, AttributeSet attrs, User user) {
        super(context, attrs);
        init(user);
    }

    private void init (@NonNull User user)
    {
        B = UserViewBinding.inflate(LayoutInflater.from(getContext()), this, true);

        B.setUser(user);
        B.userLogin.setText(user.getLogin());
    }
    public User getUser() {
        return B.getUser();
    }
    public void setActive(boolean isActive)
    {
        final int color = isActive ? R.color.input_background : R.color.white;
        B.getRoot().setBackgroundColor(getResources().getColor(color));
    }
}
