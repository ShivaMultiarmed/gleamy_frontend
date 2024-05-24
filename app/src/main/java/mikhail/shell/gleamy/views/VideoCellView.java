package mikhail.shell.gleamy.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.databinding.ImageCellViewBinding;
import mikhail.shell.gleamy.databinding.VideoCellViewBinding;
import mikhail.shell.gleamy.models.Media;

public final class VideoCellView extends MediaCellView {

    private VideoCellViewBinding B;

    public VideoCellView(Context context, Media media) {
        super(context, media);
    }

    public VideoCellView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        super(context, attrs, media);
    }

    public VideoCellView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr, media);
    }

    public VideoCellView(Context context) {
        super(context);
    }

    @Override
    protected void initBinding(Context context) {
        B = VideoCellViewBinding.inflate(LayoutInflater.from(context), this, true);
    }

    @Override
    public void setMedia(Media media, Bitmap bitmap) {
        B.setMedia(media);
        B.preview.setImageBitmap(bitmap);
    }
}