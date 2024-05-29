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

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.FragmentUserVideosBinding;
import mikhail.shell.gleamy.fragments.adapters.GridAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.ImageUtils;
import mikhail.shell.gleamy.views.VideoCellView;

public class UserVideosFragment extends GridMediaFragment<FrameLayout> {
    private FragmentUserVideosBinding B;
    public UserVideosFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
        MEDIA_TYPE = VIDEO;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = FragmentUserVideosBinding.inflate(LayoutInflater.from(getContext()),container, false);
        return B.getRoot();
    }
    @Override
    protected final void initLayoutSettings() {
        container = B.userVideosContainer;
        fragmentAdapter = new GridAdapter<VideoCellView>(getActivity(), VIDEO);
        super.initLayoutSettings();
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
}