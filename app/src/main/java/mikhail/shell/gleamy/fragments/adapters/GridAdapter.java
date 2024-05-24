package mikhail.shell.gleamy.fragments.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.VideoCellView;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private final Map<String, Media> data;
    private Media.Type MEDIA_TYPE;
    private final Context context;
    private final Map<String, Bitmap> pending;
    public GridAdapter(Context context, Media.Type type)
    {
        MEDIA_TYPE = type;
        this.context = context;
        this.data = new LinkedHashMap<>();
        this.pending = new LinkedHashMap<>();
        notifyDataSetChanged();  // ? to remove or not to remove
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final MediaCellView cell = createMediaCellView();
        return new ViewHolder(cell);
    }
    private MediaCellView createMediaCellView()
    {
        return switch (MEDIA_TYPE)
        {
            case IMAGE -> new ImageCellView(context);
            case VIDEO -> new VideoCellView(context);
            default -> null;
        };
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final List<Media> list = new ArrayList<>(data.values());
        final Media media = list.get(position);
        final Bitmap currentBitmap = pending.remove(pending.keySet().stream().findFirst().get());
        holder.setContentView(media,currentBitmap);
        /*try{
            holder.cell.addView(view);
        }
        catch (IllegalStateException e) {
            ((MediaCellView) view.getParent()).removeView(view);
            holder.cell.addView(view);
        }*/
    }
    @Override
    public int getItemCount() {
        return data.size();
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
    public void addView(Media media, Bitmap bitmap)
    {
        data.put(media.uuid,  media);
        pending.put(media.uuid, bitmap);
        notifyItemInserted(data.size() - 1);
    }
    public void removeView(String uuid)
    {
        final int position = data.values().stream().collect(Collectors.toList()).indexOf(data.get(uuid));
        data.remove(uuid);
        notifyItemRemoved(position);
    }
}
