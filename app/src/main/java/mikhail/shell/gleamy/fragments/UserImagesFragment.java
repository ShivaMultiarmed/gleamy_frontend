package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.widget.ImageView;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.fragments.adapters.ImageOverviewAdapter;
import mikhail.shell.gleamy.fragments.adapters.OverviewAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;

public class UserImagesFragment extends GridMediaFragment<ImageView>{

    public UserImagesFragment(Long userid, boolean isPrivileged) {
        super(userid, IMAGE, isPrivileged);
        btnId = R.layout.image_upload_button;
    }
    @Override
    protected final void initLayoutSettings() {
        fragmentAdapter = new ImageOverviewAdapter(getActivity());
        super.initLayoutSettings();
    }
    @Override
    protected void openMedia(Media media) {
        // TODO: open media
    }
}