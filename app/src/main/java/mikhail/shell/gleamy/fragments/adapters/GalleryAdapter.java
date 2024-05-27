package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

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
        return null;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
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
