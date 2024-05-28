package mikhail.shell.gleamy.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mikhail.shell.gleamy.fragments.ImageGalleryFragment;
import mikhail.shell.gleamy.fragments.MediaGalleryFragment;
import mikhail.shell.gleamy.models.Media.Type;

public class MediaGalleryActivity extends AppCompatActivity {
    private FrameLayout root;
    private Long userid;
    private Type MEDIA_TYPE;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createRoot();
        setContentView(root);
        receiveBundle();
        createFragment();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
    private void createRoot()
    {
        root = new FrameLayout(this);
        root.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }
    private void receiveBundle()
    {
        final Bundle bundle = getIntent().getExtras();
        userid = bundle.getLong("userid", 0);
        MEDIA_TYPE = bundle.getSerializable("MEDIA_TYPE", Type.class);
    }
    private void createFragment()
    {
        final String currentMediaId = getIntent().getStringExtra("currentMediaId");
        final boolean isPrivileged = userid == getSharedPreferences("authdetails", MODE_PRIVATE)
                        .getLong("userid", 0);
        final MediaGalleryFragment<?> fragment = switch (MEDIA_TYPE) {
            case IMAGE -> new ImageGalleryFragment(userid, isPrivileged,currentMediaId);
            case AUDIO -> null;
            case VIDEO -> null;
        };
        getSupportFragmentManager()
                .beginTransaction()
                .attach(fragment)
                .commit();
    }
}
