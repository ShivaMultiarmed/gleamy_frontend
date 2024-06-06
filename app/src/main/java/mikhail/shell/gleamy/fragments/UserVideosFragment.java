package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.VIDEO;

import android.widget.FrameLayout;

import mikhail.shell.gleamy.fragments.adapters.OverviewAdapter;
import mikhail.shell.gleamy.fragments.adapters.VideoOverviewAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.VideoCellView;

public class UserVideosFragment extends GridMediaFragment<FrameLayout> {
    public UserVideosFragment(Long userid, boolean isPrivileged) {
        super(userid, VIDEO, isPrivileged);
    }
    @Override
    protected final void initLayoutSettings() {
        fragmentAdapter = new VideoOverviewAdapter(getActivity());
        super.initLayoutSettings();
    }
    @Override
    protected void openMedia(Media media) {
        // TODO
    }
}