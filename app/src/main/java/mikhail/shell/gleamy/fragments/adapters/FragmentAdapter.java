package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.views.ImageCellView;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.VideoCellView;

public abstract class FragmentAdapter<T extends View, K extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<K> {
    protected final Map<String, Media> data;
    protected final Type MEDIA_TYPE;
    protected final Context context;
    protected final Map<String, Bitmap> pending;
    public FragmentAdapter(Context context, Type type)
    {
        MEDIA_TYPE = type;
        this.context = context;
        this.data = new LinkedHashMap<>();
        this.pending = new LinkedHashMap<>();
        notifyDataSetChanged();  // ? to remove or not to remove
    }
    protected abstract T createMediaItemView();
    @Override
    public int getItemCount() {
        return data.size();
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
    public long getLoadedMediaCount()
    {
        return data.size();
    }
}
