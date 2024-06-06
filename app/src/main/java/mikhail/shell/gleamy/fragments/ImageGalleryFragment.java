package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.widget.ImageView;

import mikhail.shell.gleamy.fragments.adapters.ImageGalleryAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;

public class ImageGalleryFragment extends MediaGalleryFragment<ImageView> {
    public ImageGalleryFragment(Long userid,boolean isPreviliged, String currentMediaId, Long mediaNumber) {
        super(userid, IMAGE, isPreviliged, currentMediaId, mediaNumber);
    }
    @Override
    protected void initLayoutSettings() {
        fragmentAdapter = new ImageGalleryAdapter(getActivity());
        super.initLayoutSettings();
    }
}
