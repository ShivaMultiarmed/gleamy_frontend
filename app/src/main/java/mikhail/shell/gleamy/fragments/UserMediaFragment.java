package mikhail.shell.gleamy.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.LinkedHashMap;
import java.util.Map;

import mikhail.shell.gleamy.GleamyApp;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;

public abstract class UserMediaFragment extends Fragment {
    protected final Map<String, Media> media;
    protected MediaViewModel mediaViewModel;
    public UserMediaFragment()
    {
        media = new LinkedHashMap<>();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaViewModel();
    }

    protected void initMediaViewModel()
    {
        Long userid = GleamyApp.getApp().getUser().getId();
        MediaViewModel.Factory mediaViewModelFactory = new MediaViewModel.Factory(this, userid);
        mediaViewModel = new ViewModelProvider(getActivity(), mediaViewModelFactory).get(MediaViewModel.class);
    }
    protected <T extends View> void squareUpView(T view)
    {
        int sideLength = 300; // B.userImagesContainer.getWidth() / COL_NUM;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(sideLength,sideLength);
        view.setLayoutParams(layoutParams);
    }
    protected abstract void fetchMediaPortion(Long portion_num);
    protected void fetchOneMedia(Media media)
    {
        mediaViewModel.fetchMediaById(media.uuid, bytes -> displayMedia(media, bytes));
    }
    protected abstract void observeMedia();
    protected abstract void displayMedia(Media media, byte[] bytes);
}
