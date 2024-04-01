package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        CardView card = (CardView) layoutInflater.inflate(R.layout.media_item, null);
        return new ViewHolder<>(card);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        List<T> list = new ArrayList<>(data.values());
        T view = list.get(position);
        holder.card.addView(view);
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public static class ViewHolder<T> extends RecyclerView.ViewHolder {
        private final CardView card;
        private T view;
        public ViewHolder(CardView card) {
            super(card);
            this.card = card;
        }
        public void setCardContent(T content)
        {
            view = content;
        }
    }
    public void addView(String uuid, T view)
    {
        data.put(uuid, view); // deal with uuid
        notifyItemInserted(0);
    }
}
