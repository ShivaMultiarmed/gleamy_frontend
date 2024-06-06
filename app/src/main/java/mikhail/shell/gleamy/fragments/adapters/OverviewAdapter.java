package mikhail.shell.gleamy.fragments.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import mikhail.shell.gleamy.activities.MediaGalleryActivity;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.MediaCellView;

public abstract class OverviewAdapter<T extends MediaCellView> extends FragmentAdapter<T>{
    public OverviewAdapter(Context context, Media.Type type) {
        super(context, type);
    }
    protected abstract T createMediaItemView();
    @Override
    public final void onBindViewHolder(@NonNull MediaViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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

}
