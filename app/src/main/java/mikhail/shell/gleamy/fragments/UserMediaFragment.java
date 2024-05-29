package mikhail.shell.gleamy.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import mikhail.shell.gleamy.fragments.adapters.FragmentAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;

public abstract class UserMediaFragment<T extends View> extends Fragment {
    protected Type MEDIA_TYPE;
    protected final int MEDIA_PORTION = 12;
    protected MediaViewModel mediaViewModel;
    protected final Long userid;
    protected final boolean isPrivileged;
    protected RecyclerView container;
    protected boolean isAllMediaLoaded;
    protected ActivityResultLauncher<String> mediaPicker;
    protected FragmentAdapter fragmentAdapter;
    public UserMediaFragment(Long userid, boolean isPreviliged)
    {
        this.userid = userid;
        this.isPrivileged = isPreviliged;
    }
    protected abstract void initLayoutSettings();
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaViewModel();
        initLayoutSettings();
        fetchMediaPortion(1L);
    }
    private void initMediaViewModel()
    {
        MediaViewModel.Factory mediaViewModelFactory = new MediaViewModel.Factory(this, userid);
        mediaViewModel = new ViewModelProvider(getActivity(), mediaViewModelFactory).get(MediaViewModel.class);
    }
    protected final void fetchMediaPortion(Long portion_num)
    {
        mediaViewModel.fetchMediaPortion(MEDIA_TYPE, portion_num, mediaList -> {
            mediaList.forEach(this::createMedia);
            if (mediaList.size() < MEDIA_PORTION)
                isAllMediaLoaded  = true;
        });
    }
    protected final void createMedia(Media media)
    {
        addMedia(media);
        mediaViewModel.fetchMediaById(media.uuid, bytes -> {if (bytes !=null) displayMedia(media, bytes);});
    }
    private void observeMedia()
    {
        mediaViewModel.observeIncomingMedia(mediaActionModel -> {
            Media media = mediaActionModel.getModel();
            if (media.type.equals(MEDIA_TYPE))
                createMedia(media);
        });
    }
    protected final void addMedia(Media media) {
        fragmentAdapter.addView(media);
    }
    protected abstract void displayMedia(Media media, byte[] bytes);
    protected abstract void removeMedia(String uuid);
    protected abstract T createItemContentFromBytes(byte[] bytes);
    protected abstract long getNextMediaPortionNumber();

}
