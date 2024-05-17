package mikhail.shell.gleamy.fragments;

import static mikhail.shell.gleamy.models.Media.Type.IMAGE;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.UserImagesFragmentBinding;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.utils.MediaUtils;
import mikhail.shell.gleamy.viewmodels.MediaViewModel;
import mikhail.shell.gleamy.viewmodels.TheUserViewModel;

public class UserImagesFragment extends GridMediaFragment<ImageView>{
    private UserImagesFragmentBinding B;
    public UserImagesFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
        MEDIA_TYPE = IMAGE;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = UserImagesFragmentBinding.inflate(inflater, container, false);
        return B.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //B.userImagesContainer.measure(0,0);
    }

    @Override
    protected void initLayoutSettings() {
        super.initLayoutSettings();

        B.userImagesContainer.setLayoutManager(layoutManager);
        B.userImagesContainer.setAdapter(gridAdapter);
        B.userImagesContainer.addItemDecoration(gridDecorator);

        if (isPrivileged)
            addUploadButton();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    private ImageView createImageViewFromBytes(byte[] bytes)
    {
        ImageView img = new ImageView(getContext());
        squareUpView(img);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        img.setImageBitmap(bitmap);
        return img;
    }
    @Override
    protected void displayMedia(Media media, byte[] bytes) {
        ImageView imageView = createImageViewFromBytes(bytes);
        gridAdapter.addView(media.uuid, imageView);

        imageView.setOnClickListener(getOnClickListener());
        imageView.setOnLongClickListener(getOnLongClickListener());
    }

    @Override
    protected void openMedia(Media media) {
        // TODO: open media
    }

    @Override
    protected void removeOneMedia(String uuid) {
        super.removeOneMedia(uuid);
        // TODO: API call, observe to check if actually removed
    }

    @Override
    protected void addUploadButton() {
        final ViewGroup root = (ViewGroup) B.getRoot();
        final FrameLayout uploadBtn = (FrameLayout) LayoutInflater.from(getContext())
                .inflate(R.layout.image_upload_button, root, false);
        root.addView(uploadBtn);
        uploadBtn.setOnClickListener(btn -> mediaPicker.launch("image/*"));
    }
    @Override
    protected RecyclerView getContainer()
    {
        return B.userImagesContainer;
    }
    @Override
    protected void initMediaPicker()
    {
        mediaPicker = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null)
                    {
                        ContentResolver contentResolver = getActivity().getContentResolver();
                        Media media = new Media.Builder()
                                .extension(MediaUtils.getExtension(contentResolver, uri))
                                .type(IMAGE)
                                .userid(userid)
                                .build();
                        try {
                            byte[] mediaBytes = MediaUtils.getFileContent(contentResolver, uri);
                            mediaViewModel.postMedia(media, mediaBytes,
                                    postedMedia -> {}
                            );
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
        );
    }
}