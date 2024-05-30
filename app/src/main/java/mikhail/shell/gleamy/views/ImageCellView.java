package mikhail.shell.gleamy.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import mikhail.shell.gleamy.R;
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
        try {
            Picasso.get()
                    .load(buildUri(media.uuid))
                    .error(R.drawable.baseline_image_not_supported_24)
                    .resize(500,500)
                    .centerCrop()
                    .into(B.image);
        } catch (Exception e)
        {
            Log.e("ImageCellView "+ media.uuid, "Image is not loaded");
            B.image.setImageResource(R.drawable.baseline_image_not_supported_24);
        }
    }
    @Override
    public Media getMedia() {
        return B.getMedia();
    }

}
