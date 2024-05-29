package mikhail.shell.gleamy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import mikhail.shell.gleamy.api.WebClient;
import mikhail.shell.gleamy.databinding.ImageCellViewBinding;
import mikhail.shell.gleamy.models.Media;

public final class ImageCellView extends MediaCellView {
    private ImageCellViewBinding B;
    public ImageCellView(Context context, Media media) {
        super(context, media);
    }
    public ImageCellView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        super(context, attrs, media);
    }
    public ImageCellView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr, media);
    }
    public ImageCellView(Context context) {
        super(context);
    }
    @Override
    protected void initBinding(Context context) {
        B = ImageCellViewBinding.inflate(LayoutInflater.from(context), this, true);
    }
    @Override
    public void setMedia(Media media) {
        B.setMedia(media);
        Picasso.get().load(buildUri(media.uuid)).into(B.image);
    }
    @Override
    public Media getMedia() {
        return B.getMedia();
    }

}
