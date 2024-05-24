package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.VIDEO;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.HashMap;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.FragmentUserVideosBinding;
import mikhail.shell.gleamy.fragments.adapters.GridAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.ImageUtils;
import mikhail.shell.gleamy.views.Tab;

public class UserVideosFragment extends GridMediaFragment<FrameLayout> {
    private FragmentUserVideosBinding B;
    public UserVideosFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
        MEDIA_TYPE = VIDEO;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentUserVideosBinding.inflate(LayoutInflater.from(getContext()),container, false);
        return B.getRoot();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    protected final void initLayoutSettings() {
        super.initLayoutSettings();

        gridAdapter = new GridAdapter(getActivity(), VIDEO);

        B.userVideosContainer.setLayoutManager(layoutManager);
        B.userVideosContainer.setAdapter(gridAdapter);
        B.userVideosContainer.addItemDecoration(gridDecorator);

        if (isPrivileged)
            addUploadButton();
    }
    @Override
    protected void displayMedia(Media media, byte[] bytes) {
        final FrameLayout frameLayout = createItemContentFromBytes(bytes);
        gridAdapter.addView(media, ImageUtils.getBitmap(bytes));
        initListeners(frameLayout, media);
    }
    @Override
    protected void openMedia(Media media) {
        // TODO
    }
    @Override
    protected void addUploadButton() {
        final ViewGroup root = (ViewGroup) B.getRoot();
        final FrameLayout uploadBtn = (FrameLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.video_upload_button, root, false);
        root.addView(uploadBtn);
        uploadBtn.setOnClickListener(btn -> mediaPicker.launch("video/*"));
    }
    @Override
    protected FrameLayout createItemContentFromBytes(byte[] bytes) {
        final ImageView videoPreview = new ImageView(getContext());
        squareUpView(videoPreview);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        videoPreview.setImageBitmap(bitmap);

        final ImageView playButton = new ImageView(getContext());
        playButton.setImageResource(R.drawable.play_button_transparent);

        final FrameLayout itemContent = new FrameLayout(getContext());
        itemContent.addView(videoPreview);
        itemContent.addView(playButton);
        return itemContent;
    }
}