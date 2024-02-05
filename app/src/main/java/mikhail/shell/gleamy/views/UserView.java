package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.User;


public class UserView extends LinearLayout {
    private User user;
    private TextView login;

    public UserView(Context context) {
        super(context);
    }
    public UserView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init ()
    {
        login = findViewById(R.id.userLogin);
    }

    public User getUser() {
        return user;
    }

    public void setUser(@NonNull User user) {
        this.user = user;
        login.setText(user.getLogin());
    }

}
