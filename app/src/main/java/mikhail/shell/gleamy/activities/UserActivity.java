package mikhail.shell.gleamy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.HashMap;
import java.util.Map;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ActivityUserBinding;
import mikhail.shell.gleamy.fragments.UserImagesFragment;
import mikhail.shell.gleamy.fragments.UserVideosFragment;
import mikhail.shell.gleamy.viewmodels.TheUserViewModel;

public class UserActivity extends AppCompatActivity {

    private ActivityUserBinding B;
    private TheUserViewModel userViewModel;
    private final static Map<Integer, Class<? extends Fragment>> tabLayouts = new HashMap<>();
    static
    {
        tabLayouts.put(R.id.userImgsTab, UserImagesFragment.class);
        tabLayouts.put(R.id.userVidsTab, UserVideosFragment.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        B = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(B.getRoot());

        long userid = getIntent().getLongExtra("userid", 0);
        TheUserViewModel.UserViewModelFactory factory = new TheUserViewModel.UserViewModelFactory(userid);
        userViewModel = new ViewModelProvider(this, factory)
                .get(TheUserViewModel.class);
        observeUser();

        initTabs(changeTabListener());
    }
    private void observeUser()
    {
        userViewModel.getUser(user -> {
            Log.i("UserActivity", user.toString());
            B.setUser(user);
        });
    }
    private <T extends Fragment> View.OnClickListener changeTabListener()
    {
        return tab ->getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(B.userContent.getId(), tabLayouts.get(tab.getId()), null)
                .commit();
    }
    private void initTabs(View.OnClickListener listener)
    {
        for (int i=0; i< B.tabsBar.getChildCount(); i++)
            B.tabsBar.getChildAt(i).setOnClickListener(listener);
    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}