package mikhail.shell.gleamy.fragments.adapters;

import static mikhail.shell.gleamy.models.Media.Type.VIDEO;

import android.content.Context;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.VideoCellView;

public class VideoOverviewAdapter extends OverviewAdapter<VideoCellView>{
    public VideoOverviewAdapter(Context context) {
        super(context, VIDEO);
    }
    @Override
    protected VideoCellView createMediaItemView() {
        return new VideoCellView(context);
    }
}
