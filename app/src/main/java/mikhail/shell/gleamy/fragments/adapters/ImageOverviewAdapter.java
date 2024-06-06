package mikhail.shell.gleamy.fragments.adapters;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.content.Context;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;

public class ImageOverviewAdapter extends OverviewAdapter<ImageCellView>{
    public ImageOverviewAdapter(Context context) {
        super(context, IMAGE);
    }
    @Override
    protected ImageCellView createMediaItemView() {
        return new ImageCellView(context);
    }
}
