package mikhail.shell.gleamy.fragments;

import android.content.ContentResolver;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.MediaUtils;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;

public abstract class UserMediaFragment<T extends View> extends Fragment {
    protected Media.Type MEDIA_TYPE;
    protected final Map<String, Media> media;
    protected MediaViewModel mediaViewModel;
    protected final Long userid;
    protected final boolean isPrivileged;
    protected ActivityResultLauncher<String> mediaPicker;
    public UserMediaFragment(Long userid, boolean isPreviliged)
    {
        this.userid = userid;
        this.isPrivileged = isPreviliged;
        media = new LinkedHashMap<>();
    }
    protected abstract void initLayoutSettings();

    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaViewModel();
        initLayoutSettings();
        initMediaPicker();
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
            final Map<String, Media> mediaPortion = new HashMap<>();
            mediaList.forEach(media -> {
                mediaPortion.put(media.uuid, media);
                fetchOneMedia(media);
            });
        });
    }
    protected final void fetchOneMedia(Media media)
    {
        mediaViewModel.fetchMediaById(media.uuid, bytes -> displayMedia(media, bytes));
    }
    private void observeMedia()
    {
        mediaViewModel.observeIncomingMedia(mediaActionModel -> {
            Media media = mediaActionModel.getModel();
            if (media.type.equals(MEDIA_TYPE))
                fetchOneMedia(media);
        });
    }
    protected abstract void displayMedia(Media media, byte[] bytes);
    protected final void postMedia(Media media, byte[] bytes)
    {
        mediaViewModel.postMedia(media, bytes, responseMedia -> {
            if (responseMedia != null)
                displayMedia(responseMedia, bytes);
            else
                Toast.makeText(requireActivity(), "Ошибка при публикации", Toast.LENGTH_SHORT).show();
        });
    }
    protected abstract void removeOneMedia(String uuid);
    protected abstract void openMedia(Media media);
    protected abstract void addUploadButton();
    protected final void initListeners(T view, Media media)
    {
        view.setOnClickListener(v -> openMedia(media));
        view.setOnLongClickListener(v -> {
            removeOneMedia(null);
            return false;
        });
    }
    private void initMediaPicker()
    {
        mediaPicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null)
                    {
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        Media media = new Media.Builder()
                                .extension(MediaUtils.getExtension(contentResolver, uri))
                                .type(MEDIA_TYPE)
                                .userid(userid)
                                .build();
                        try {
                            byte[] mediaBytes = MediaUtils.getFileContent(contentResolver, uri);
                            postMedia(media, mediaBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
    protected abstract T createItemContentFromBytes(byte[] bytes);
}
