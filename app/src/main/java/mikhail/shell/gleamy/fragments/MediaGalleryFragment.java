package mikhail.shell.gleamy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import mikhail.shell.gleamy.databinding.MediaGalleryFragmentBinding;
import mikhail.shell.gleamy.fragments.adapters.GalleryAdapter;

public abstract class MediaGalleryFragment<T extends View> extends UserMediaFragment<T> {
    protected MediaGalleryFragmentBinding B;
    protected GalleryAdapter<T> galleryAdapter;
    protected final String currentMediaId;
    public MediaGalleryFragment(Long userid, boolean isPreviliged, String currentMediaId) {
        super(userid, isPreviliged);
        this.currentMediaId = currentMediaId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        B = MediaGalleryFragmentBinding.inflate(LayoutInflater.from(getContext()), container, false);
        return B.getRoot();
    }
    @Override
    protected long getNextMediaPortionNumber() {
        return (long) Math.ceil(galleryAdapter.getLoadedMediaCount() * 1.0 / MEDIA_PORTION) + 1L;
    }
}
