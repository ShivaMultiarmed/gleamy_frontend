package mikhail.shell.gleamy.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import java.io.IOException;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.activities.MediaGalleryActivity;
import mikhail.shell.gleamy.databinding.MediaOverviewFragmentBinding;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.MediaUtils;
import mikhail.shell.gleamy.views.MediaCellView;

public abstract class MediaOverviewFragment<T extends View> extends UserMediaFragment<RecyclerView, T> {
    protected MediaOverviewFragmentBinding B;
    protected int btnId;
    protected RecyclerView.LayoutManager layoutManager;
    public MediaOverviewFragment(Long userid, Media.Type mediaType, boolean isPreviliged) {
        super(userid, mediaType, isPreviliged);
    }
    protected abstract void openMedia(Media media);
    protected final void initListeners(T view, Media media)
    {
        view.setOnClickListener(v -> openMedia(media));
        view.setOnLongClickListener(v -> {
            removeMedia(null);
            return false;
        });
    }
    @Override
    public final View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        wrapper = (FrameLayout) container;
        B = MediaOverviewFragmentBinding.inflate(LayoutInflater.from(getContext()),container, false);
        return B.getRoot();
    }
    @Override
    public final void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaPicker();
        setOnContainerScroll();
        fetchMediaPortion(1L);
    }
    private void initMediaPicker()
    {
        mediaPicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null)
                    {
                        final ContentResolver contentResolver = getActivity().getContentResolver();
                        final Media media = new Media.Builder()
                                .extension(MediaUtils.getExtension(contentResolver, uri))
                                .type(MEDIA_TYPE)
                                .userid(userid)
                                .build();
                        try {
                            final byte[] mediaBytes = MediaUtils.getFileContent(contentResolver, uri);
                            postMedia(media, mediaBytes);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
    private void setOnContainerScroll()
    {
        container.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (container.canScrollVertically(1))
                    if (!isAllMediaLoaded)
                    {
                        final long portionNumber = getNextMediaPortionNumber();
                        fetchMediaPortion(portionNumber);
                    }
            }
        });
    }
    private void postMedia(Media media, byte[] bytes)
    {
        mediaViewModel.postMedia(media, bytes, responseMedia -> {
            if (responseMedia != null)
                addMedia(responseMedia);
            else
                Toast.makeText(requireActivity(), "Ошибка при публикации", Toast.LENGTH_SHORT).show();
        });
    }
    private void addUploadButton() {
        final FrameLayout uploadBtn = (FrameLayout) LayoutInflater.from(getContext())
                .inflate(btnId, wrapper, false);
        wrapper.addView(uploadBtn);
        uploadBtn.setOnClickListener(btn -> mediaPicker.launch(MEDIA_TYPE.mime));
    }
    @Override
    protected void initLayoutSettings() {
        container = B.mediaContainer;
        container.setAdapter(fragmentAdapter);
        container.setLayoutManager(layoutManager);
        if (isPrivileged)
            addUploadButton();
    }
}
