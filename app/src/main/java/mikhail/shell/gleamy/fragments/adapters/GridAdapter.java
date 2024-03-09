package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Map;

import mikhail.shell.gleamy.R;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {
    private Map<Long, ImageView> data;
    private final Context context;
    public GridAdapter(Context context, Map<Long, ImageView> data)
    {
        this.context = context;
        this.data = data;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView image = new ImageView(context);
        return new ViewHolder(image);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView image = data.get(position);
        holder.image.setImageDrawable(image.getDrawable());
        Log.i("GridAdapter", "on bind actions are here");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            image  = (ImageView) itemView;
        }
    }
}
