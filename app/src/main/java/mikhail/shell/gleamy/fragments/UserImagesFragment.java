package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.UserImagesFragmentBinding;
import mikhail.shell.gleamy.fragments.adapters.GridAdapter;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.ImageCellView;

public class UserImagesFragment extends GridMediaFragment<ImageView>{
    private UserImagesFragmentBinding B;
    public UserImagesFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
        MEDIA_TYPE = IMAGE;
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = UserImagesFragmentBinding.inflate(inflater, container, false);
        return B.getRoot();
    }
    @Override
    protected final void initLayoutSettings() {
        container = B.userImagesContainer;
        fragmentAdapter = new GridAdapter<ImageCellView>(getActivity(), IMAGE);
        super.initLayoutSettings();
    }
    @Override
    protected void openMedia(Media media) {
        // TODO: open media
    }
    @Override
    protected void addUploadButton() {
        final ViewGroup root = (ViewGroup) B.getRoot();
        final FrameLayout uploadBtn = (FrameLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.image_upload_button, root, false);
        root.addView(uploadBtn);
        uploadBtn.setOnClickListener(btn -> mediaPicker.launch("image/*"));
    }
}