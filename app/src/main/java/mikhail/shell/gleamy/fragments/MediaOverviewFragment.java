package mikhail.shell.gleamy.fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;

import mikhail.shell.gleamy.activities.MediaGalleryActivity;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.MediaUtils;
import mikhail.shell.gleamy.views.MediaCellView;

public abstract class MediaOverviewFragment<T extends View> extends UserMediaFragment<T> {
    public MediaOverviewFragment(Long userid, boolean isPreviliged) {
        super(userid, isPreviliged);
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMediaPicker();
        setOnContainerScroll();
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
    protected final void postMedia(Media media, byte[] bytes)
    {
        mediaViewModel.postMedia(media, bytes, responseMedia -> {
            if (responseMedia != null)
                addMedia(responseMedia);
            else
                Toast.makeText(requireActivity(), "Ошибка при публикации", Toast.LENGTH_SHORT).show();
        });
    }
    protected abstract void addUploadButton();
    private void onClick(MediaCellView cell)
    {
        final Intent intent = new Intent(requireActivity(), MediaGalleryActivity.class);
        final Bundle bundle = new Bundle();
        bundle.putString("currentMediaId", cell.getMedia().uuid);
        bundle.putLong("userid", userid);
        bundle.putSerializable("MEDIA_TYPE", MEDIA_TYPE);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
