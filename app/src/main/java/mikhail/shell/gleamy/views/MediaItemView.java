package mikhail.shell.gleamy.views;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.models.Media;

public abstract class MediaItemView extends FrameLayout {
    public MediaItemView(Context context)
    {
        this(context, null, 0, null);
    }
    public MediaItemView(Context context, Media media)
    {
        this(context, null, 0, media);
    }
    public MediaItemView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        this(context, attrs, 0, media);
    }
    public MediaItemView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr);
        initBinding(context);
    }
    protected abstract void initBinding(Context context);
    public abstract void setMedia(Media media);
    public abstract Media getMedia();
    protected abstract Uri buildUri(String uuid);
}
