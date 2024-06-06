package mikhail.shell.gleamy.fragments.adapters;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.content.Context;

import mikhail.shell.gleamy.views.ImageGalleryView;

public class ImageGalleryAdapter extends GalleryAdapter<ImageGalleryView>{
    public ImageGalleryAdapter(Context context) {
        super(context, IMAGE);
    }
    @Override
    protected ImageGalleryView createMediaItemView() {
        return new ImageGalleryView(context);
    }
}
