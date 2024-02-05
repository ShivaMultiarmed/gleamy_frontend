package mikhail.shell.gleamy.models;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import mikhail.shell.gleamy.R;


public class User extends LinearLayout {
    private UserInfo user;
    private TextView login;

    public User(Context context) {
        super(context);
    }
    public User(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public User(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init ()
    {
        login = findViewById(R.id.userLogin);
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(@NonNull UserInfo user) {
        this.user = user;
        login.setText(user.getLogin());
    }

}
