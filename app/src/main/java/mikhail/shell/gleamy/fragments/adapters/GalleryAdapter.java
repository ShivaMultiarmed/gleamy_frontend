package mikhail.shell.gleamy.fragments.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.views.MediaCellView;

public class GalleryAdapter<T extends View> extends FragmentAdapter<T, GalleryAdapter.ViewHolder> {
    public GalleryAdapter(Context context, Type type) {
        super(context, type);
    }

    @Override
    protected T createMediaItemView() {
        switch (MEDIA_TYPE)
        {
            case AUDIO -> { return null; }
            case IMAGE -> {
                final ImageView imageView = new ImageView(context);
                imageView.setLayoutParams(new RecyclerView.LayoutParams(MATCH_PARENT, 0));
                imageView.setScaleType(CENTER_CROP);
                return (T) imageView;
            }
            case VIDEO -> { return null; }
            default -> { return null; }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(createMediaItemView());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View item;
        public ViewHolder(View item) {
            super(item);
            this.item = item;
        }
        public void setContentView(Media media, Bitmap bitmap)
        {
            //item.setMedia(media, bitmap);
        }
    }
}
