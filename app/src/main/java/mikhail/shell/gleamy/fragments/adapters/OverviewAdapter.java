package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mikhail.shell.gleamy.activities.MediaGalleryActivity;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.VideoCellView;

public class OverviewAdapter<T extends MediaCellView> extends FragmentAdapter<T, OverviewAdapter.ViewHolder>{ // TODO generify
    public OverviewAdapter(Context context, Media.Type type) {
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
        holder.cell.setMedia(media);
        holder.cell.setOnClickListener(item -> onClick((MediaCellView) item));
    }
    private void onClick(MediaCellView cell)
    {
        final Intent intent = new Intent(context, MediaGalleryActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString("currentMediaId", cell.getMedia().uuid);
        bundle.putLong("userid", cell.getMedia().userid);
        bundle.putLong("mediaNumber", data.indexOf(cell.getMedia()));
        bundle.putSerializable("MEDIA_TYPE", MEDIA_TYPE);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final MediaCellView cell;
        public ViewHolder(MediaCellView cell) {
            super(cell);
            this.cell = cell;
        }
    }
}
