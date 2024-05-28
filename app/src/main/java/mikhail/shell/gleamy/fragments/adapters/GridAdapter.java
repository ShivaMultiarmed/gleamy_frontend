package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.VideoCellView;

public class GridAdapter<T extends MediaCellView> extends FragmentAdapter<T, GridAdapter.ViewHolder>{ // TODO generify
    public GridAdapter(Context context, Media.Type type) {
        super(context, type);
    }
    protected T createMediaItemView()
    {
        return switch (MEDIA_TYPE)
                {
                    case IMAGE -> (T) new ImageCellView(context);
                    case VIDEO -> (T) new VideoCellView(context);
                    default -> null;
                };
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(createMediaItemView());
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Media media = data.get(position);
        final Bitmap currentBitmap = (Bitmap) pending.get(position);
        ((ViewHolder) holder).setContentView(media,currentBitmap); // TODO: change bitmap to byte[]
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MediaCellView cell;
        public ViewHolder(MediaCellView cell) {
            super(cell);
            this.cell = cell;
        }
        public void setContentView(Media media, Bitmap bitmap)
        {
            cell.setMedia(media, bitmap);
        }
    }
}
