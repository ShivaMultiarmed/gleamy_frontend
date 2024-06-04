package mikhail.shell.gleamy.activities;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mikhail.shell.gleamy.R;
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
        setContentView(R.layout.media_gallery_activity);
        root = findViewById(R.id.media_gallery_wrapper);
        receiveBundle();
        createFragment();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    private void receiveBundle()
    {
        final Bundle bundle = getIntent().getExtras();
        userid = bundle.getLong("userid", 0);
        MEDIA_TYPE = (Type) bundle.getSerializable("MEDIA_TYPE");
    }
    private void createFragment()
    {
        final String currentMediaId = getIntent().getStringExtra("currentMediaId");
        final Long mediaNumber = getIntent().getLongExtra("mediaNumber", 0);
        final boolean isPrivileged = userid == getSharedPreferences("authdetails", MODE_PRIVATE)
                        .getLong("userid", 0);
        final MediaGalleryFragment<?> fragment = switch (MEDIA_TYPE) {
            case IMAGE -> new ImageGalleryFragment(userid, isPrivileged, currentMediaId, mediaNumber);
            case AUDIO -> null;
            case VIDEO -> null;
        };

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .add(root.getId(), fragment)
                .commit();
    }
}
