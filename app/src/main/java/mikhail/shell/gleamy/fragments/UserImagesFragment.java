package mikhail.shell.gleamy.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.LongStream;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.databinding.UserImagesFragmentBinding;
import mikhail.shell.gleamy.fragments.adapters.GridAdapter;
import mikhail.shell.gleamy.models.Media;

public class UserImagesFragment extends GridMediaFragment<ImageView>{
    private UserImagesFragmentBinding B;
    public UserImagesFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
        MEDIA_TYPE = Media.Type.IMAGE;
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

}