package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.views.ImageCellView;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.VideoCellView;

public abstract class FragmentAdapter<T extends View, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {
    protected final List<Media> data;
    protected final Type MEDIA_TYPE;
    protected final Context context;
    protected final List<Bitmap> pending;
    public FragmentAdapter(Context context, Type type)
    {
        MEDIA_TYPE = type;
        this.context = context;
        this.data = new LinkedList<>();
        this.pending = new LinkedList<>();
        notifyDataSetChanged();  // ? to remove or not to remove
    }
    protected abstract T createMediaItemView();
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void addView(final Media media)
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
        pending.add(position, null);
        notifyItemInserted(position);
    }
    public void addItemContent(final Media media, final Bitmap bitmap)
    {
        final int position = data.indexOf(media);
        pending.set(position, bitmap);
        notifyItemChanged(position);
    }
    public void removeView(final String uuid)
    {
        final Media m = data.stream().filter(media -> media.uuid.equals(uuid)).findFirst().orElse(null);
        final int position = data.indexOf(m);
        data.remove(position);
        pending.remove(position);
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
}
