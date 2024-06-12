package mikhail.shell.gleamy.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import mikhail.shell.gleamy.fragments.adapters.FragmentAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;

public abstract class UserMediaFragment<K extends ViewGroup, T extends View> extends Fragment {
    protected final Type MEDIA_TYPE;
    protected final int MEDIA_PORTION = 12;
    protected MediaViewModel mediaViewModel;
    protected final Long userid;
    protected final boolean isPrivileged;
    protected K container;
    protected boolean isLastMediaLoaded = false, isFirstMediaLoaded = false;
    protected ActivityResultLauncher<String> mediaPicker;
    protected FragmentAdapter fragmentAdapter;
    protected FrameLayout wrapper;
    public UserMediaFragment(Long userid, Type mediaType, boolean isPreviliged)
    {
        this.userid = userid;
        this.isPrivileged = isPreviliged;
        MEDIA_TYPE = mediaType;
    }
    protected abstract void initLayoutSettings();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaViewModel();
        initLayoutSettings();
        // TODO: add fetchMediaPortion(currentNumber)
    }
    private void initMediaViewModel()
    {
        MediaViewModel.Factory mediaViewModelFactory = new MediaViewModel.Factory(this, userid);
        mediaViewModel = new ViewModelProvider(getActivity(), mediaViewModelFactory).get(MediaViewModel.class);
    }
    protected final void fetchMediaPortion(Long portion_num)
    {
        mediaViewModel.fetchMediaPortion(MEDIA_TYPE, portion_num, mediaList -> {
            mediaList.forEach(this::addMedia);
            if (mediaList.size() < MEDIA_PORTION)
                isLastMediaLoaded = true;
            if (portion_num == 1)
                isFirstMediaLoaded = true;
        });
    }
    private void observeMedia()
    {
        mediaViewModel.observeIncomingMedia(mediaActionModel -> {
            Media media = mediaActionModel.getModel();
            if (media.type.equals(MEDIA_TYPE))
                addMedia(media);
        });
    }
    protected final void addMedia(Media media) {
        fragmentAdapter.addView(media);
    }
    protected final void removeMedia(String uuid) {
        fragmentAdapter.removeView(uuid);
    }
    protected abstract Long getCurrentPortionNumber();
    protected abstract void setOnChangeListener();
}
