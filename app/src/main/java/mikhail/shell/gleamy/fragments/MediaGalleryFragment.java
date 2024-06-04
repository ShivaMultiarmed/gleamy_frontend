package mikhail.shell.gleamy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import mikhail.shell.gleamy.databinding.MediaGalleryFragmentBinding;
import mikhail.shell.gleamy.fragments.adapters.GalleryAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;

public abstract class MediaGalleryFragment<T extends View> extends UserMediaFragment<ViewPager2, T> {
    protected MediaGalleryFragmentBinding B;
    protected final String currentMediaId;
    protected Long mediaNumber;
    public MediaGalleryFragment(Long userid, Type mediaType, boolean isPreviliged, String currentMediaId, Long mediaNumber) {
        super(userid, mediaType, isPreviliged);
        this.currentMediaId = currentMediaId;
        this.mediaNumber = mediaNumber;
    }
    @NonNull
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wrapper = (FrameLayout) container;
        B = MediaGalleryFragmentBinding.inflate(LayoutInflater.from(getContext()), container, false);
        return B.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Long currentPortionNumber = getCurrentPortionNumber();
        fetchMediaPortion(currentPortionNumber);
    }
    @Override
    protected void initLayoutSettings() {
        container = B.mediaContainer;
        container.setAdapter(null); // TODO: create an adapter
    }
    private Long getCurrentPortionNumber()
    {
        return (long) Math.ceil(mediaNumber * 1.0 / MEDIA_PORTION);
    }
}
