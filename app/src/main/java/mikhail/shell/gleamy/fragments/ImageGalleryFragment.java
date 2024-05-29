package mikhail.shell.gleamy.fragments;

import android.widget.ImageView;

import mikhail.shell.gleamy.models.Media;

public class ImageGalleryFragment extends MediaGalleryFragment<ImageView> {
    public ImageGalleryFragment(Long userid, boolean isPreviliged, String currentMediaId) {
        super(userid, isPreviliged, currentMediaId);
    }
    @Override
    protected void initLayoutSettings() {
        // TODO
    }
}
