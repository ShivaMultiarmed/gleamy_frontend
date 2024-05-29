package mikhail.shell.gleamy.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.databinding.MediaCellBinding;
import mikhail.shell.gleamy.models.Media;

public abstract class MediaCellView extends FrameLayout {
    public MediaCellView(Context context)
    {
        this(context, null, 0, null);
    }
    public MediaCellView(Context context, Media media)
    {
        this(context, null, 0, media);
    }
    public MediaCellView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        this(context, attrs, 0, media);
    }
    public MediaCellView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr);
        initBinding(context);
    }
    protected abstract void initBinding(Context context);
    public abstract void setMedia(Media media);
    public abstract Media getMedia();
    protected final Uri buildUri(String uuid) {
        // TODO change to media preview endpoint
        return Uri.parse("http://158.160.22.54:8080/gleamy/api/v1/users/media?uuid="+uuid);
    }
}
