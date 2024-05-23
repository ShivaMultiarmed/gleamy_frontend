package mikhail.shell.gleamy.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.databinding.MediaItemBinding;
import mikhail.shell.gleamy.models.Media;

public class MediaCellView extends FrameLayout {
    private MediaItemBinding B;
    public MediaCellView(Context context, Media media)
    {
        this(context, null, 0, media);
    }
    public MediaCellView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        this(context, attrs, 0, media);
    }
    public MediaCellView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr);
        B = MediaItemBinding.inflate(LayoutInflater.from(context), this, true);
        B.setMedia(media);
    }
}
