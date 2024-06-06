package mikhail.shell.gleamy.fragments.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.MediaItemView;

public abstract class FragmentAdapter<T extends MediaItemView>
        extends RecyclerView.Adapter<FragmentAdapter.MediaViewHolder> {
    protected final List<Media> data;
    protected final Type MEDIA_TYPE;
    protected final Context context;
    public FragmentAdapter(Context context, Type type)
    {
        MEDIA_TYPE = type;
        this.context = context;
        data = new LinkedList<>();
    }
    protected abstract T createMediaItemView();
    @NonNull
    @Override
    public final MediaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MediaViewHolder(createMediaItemView());
    }
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        final Media media = data.get(position);
        holder.cell.setMedia(media);
    }
    @Override
    public final int getItemCount() {
        return data.size();
    }
    public final void addView(final Media media)
    {
        final Media earlierMedia;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            earlierMedia = data.stream()
                    .filter(media1 -> media1.date_time.compareTo(media.date_time) < 0)
                    .findFirst().orElse(null);
        }
        else
            earlierMedia = null;
        final int position = (earlierMedia == null) ? data.size() : data.indexOf(earlierMedia);
        data.add(position, media);
        notifyItemInserted(position);
    }
    public final void removeView(final String uuid)
    {
        final Media m = data.stream().filter(media -> media.uuid.equals(uuid)).findFirst().orElse(null);
        final int position = data.indexOf(m);
        data.remove(position);
        notifyItemRemoved(position);
    }
    public long getLoadedMediaCount()
    {
        return data.size();
    }
    public boolean hasItemWithId(final String uuid)
    {
        return data.stream().filter(media -> media.uuid.equals(uuid)).findFirst().orElse(null) != null;
    }
    public final static class MediaViewHolder extends RecyclerView.ViewHolder {
        public final MediaItemView cell;
        public MediaViewHolder(MediaItemView cell) {
            super(cell);
            this.cell = cell;
        }
    }
}
