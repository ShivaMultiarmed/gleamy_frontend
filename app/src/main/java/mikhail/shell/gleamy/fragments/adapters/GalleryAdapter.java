package mikhail.shell.gleamy.fragments.adapters;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.widget.ImageView.ScaleType.CENTER_CROP;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.views.MediaCellView;
import mikhail.shell.gleamy.views.MediaItemView;

public abstract class GalleryAdapter<T extends MediaItemView> extends FragmentAdapter<T> {
    public GalleryAdapter(Context context, Type type) {
        super(context, type);
    }

    @Override
    protected abstract T createMediaItemView();
    @Override
    public void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        //holder.cell.setLayoutParams(new FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT));
    }
}
