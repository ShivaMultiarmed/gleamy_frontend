package mikhail.shell.gleamy.views;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.ImageGalleryViewBinding;
import mikhail.shell.gleamy.models.Media;

public final class ImageGalleryView extends GalleryItemView {
    private ImageGalleryViewBinding B;
    public ImageGalleryView(Context context, Media media) {
        super(context, media);
    }
    public ImageGalleryView(@NonNull Context context, @Nullable AttributeSet attrs, Media media) {
        super(context, attrs, media);
    }
    public ImageGalleryView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, Media media) {
        super(context, attrs, defStyleAttr, media);
    }
    public ImageGalleryView(Context context) {
        super(context);
    }
    @Override
    protected void initBinding(Context context) {
        B = ImageGalleryViewBinding.inflate(LayoutInflater.from(context), this, true);
    }
    @Override
    public void setMedia(Media media) {
        B.setMedia(media);
        try {
            Picasso.get()
                    .load(buildUri(media.uuid))
                    .error(R.drawable.baseline_image_not_supported_24)
                    .resize(500, 1000)
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
