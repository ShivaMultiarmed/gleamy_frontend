package mikhail.shell.gleamy.fragments.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_CROP;
import static android.widget.ImageView.ScaleType.FIT_XY;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mikhail.shell.gleamy.R;

public class GridAdapter<T extends View> extends RecyclerView.Adapter<GridAdapter.ViewHolder<T>> {
    private final Map<String, T> data;
    private final Context context;
    public GridAdapter(Context context, Map<String, T> data)
    {
        this.context = context;
        this.data = data;
        notifyDataSetChanged();  // ? to remove or not to remove
    }
    @NonNull
    @Override
    public ViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        FrameLayout cell = (FrameLayout) layoutInflater.inflate(R.layout.media_item, null);
        return new ViewHolder<>(cell);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<T> list = new ArrayList<>(data.values());
        T view = list.get(position);
        holder.cell.addView(view);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        private final FrameLayout cell;
        private T view;
        public ViewHolder(FrameLayout cell) {
            super(cell);
            this.cell = cell;
        }
        public void setCardContent(T content)
        {
            view = content;
        }
    }
    public void addView(String uuid, T view)
    {
        data.put(uuid, view);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(MATCH_PARENT, 300);
        view.setLayoutParams(params);
        ((ImageView) view).setScaleType(CENTER_CROP);
        notifyItemInserted(data.size() - 1);
    }
    public void removeView(String uuid)
    {
        int position = data.values().stream().collect(Collectors.toList()).indexOf(data.get(uuid));
        data.remove(uuid);
        notifyItemRemoved(position);
    }
}
