package mikhail.shell.gleamy.views;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.models.Media;

public abstract class GalleryItemView extends MediaItemView {
    public GalleryItemView(Context context)
    {
        this(context, null, 0, null);
    }
    public GalleryItemView(Context context, Media media)
    {
        this(context, null, 0, media);
    }
    public GalleryItemView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        this(context, attrs, 0, media);
    }
    public GalleryItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr, media);
        setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }
    @Override
    protected final Uri buildUri(String uuid) {
        return Uri.parse("http://158.160.22.54:8080/gleamy/api/v1/users/media?uuid="+uuid);
    }
}
