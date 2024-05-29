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
    protected RecyclerView.LayoutManager layoutManager;
    protected RecyclerView.ItemDecoration decorator;
    public UserMediaFragment(Long userid, boolean isPreviliged)
    {
        this.userid = userid;
        this.isPrivileged = isPreviliged;
    }
    protected void initLayoutSettings(){
        container.setLayoutManager(layoutManager);
        container.setAdapter(fragmentAdapter);
        container.addItemDecoration(decorator);
    }
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
            mediaList.forEach(this::addMedia);
            if (mediaList.size() < MEDIA_PORTION)
                isAllMediaLoaded  = true;
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
    protected final long getNextMediaPortionNumber()
    {
        return (long) Math.ceil(fragmentAdapter.getLoadedMediaCount() * 1.0 / MEDIA_PORTION) + 1L;
    }
}
