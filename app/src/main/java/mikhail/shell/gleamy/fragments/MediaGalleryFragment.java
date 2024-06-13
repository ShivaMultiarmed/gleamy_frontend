package mikhail.shell.gleamy.fragments;

import static android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_SETTLING;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback;

import java.util.List;

import mikhail.shell.gleamy.databinding.MediaGalleryFragmentBinding;
import mikhail.shell.gleamy.fragments.adapters.GalleryAdapter;
import mikhail.shell.gleamy.fragments.adapters.ImageGalleryAdapter;
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
        setOnChangeListener();
        fetchMediaPortion(currentPortionNumber);
    }

    @Override
    protected void setMediaQuantity(Long quantity) {
        super.setMediaQuantity(quantity);
        B.setTotalMediaNumber(MEDIA_QUANTITY);
    }

    @Override
    protected void initLayoutSettings() {
        container = B.mediaContainer;
        container.setAdapter(fragmentAdapter);
    }
    protected final Long getCurrentPortionNumber()
    {
        return (long) Math.floor(mediaNumber * 1.0 / MEDIA_PORTION) + 1L;
    }

    @Override
    protected final void setOnChangeListener() {
        container.registerOnPageChangeCallback(new OnPageChangeCallback() {
            private int oldPos = mediaNumber.intValue() % MEDIA_PORTION;
            @Override
            public void onPageSelected(int newPos) {
                super.onPageScrollStateChanged(newPos);

                if (newPos > oldPos)
                    mediaNumber++;
                else if (newPos < oldPos)
                    mediaNumber--;

                final Long currentPortionNumber = getCurrentPortionNumber();
                if (newPos == 0 && currentPortionNumber != 1 && !isFirstMediaLoaded){
                    fetchMediaPortion(currentPortionNumber - 1);
                }
                else if (newPos == fragmentAdapter.getItemCount()-1 && !isLastMediaLoaded)
                {
                    fetchMediaPortion(currentPortionNumber + 1);
                }

                B.setCurrentMediaNumber(mediaNumber + 1);
                oldPos = newPos;
            }
        });
    }
    @Override
    protected final void processMediaPortion(Long portion_num, List<Media> mediaList) {
        super.processMediaPortion(portion_num, mediaList);
        if (!isPrimaryPortionLoaded)
        {
            isPrimaryPortionLoaded = true;
            final int primaryPos = mediaNumber.intValue() % MEDIA_PORTION;
            container.setCurrentItem(primaryPos, false);
        }
    }
}
