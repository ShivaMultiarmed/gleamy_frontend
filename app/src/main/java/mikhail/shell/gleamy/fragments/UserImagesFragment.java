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

public class UserImagesFragment extends UserMediaFragment {
    private UserImagesFragmentBinding B;
    private final static int COL_NUM = 3;
    public UserImagesFragment() {
        super();
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

        fetchMediaPortion(1L);

        //B.userImagesContainer.measure(0,0);

        Map<String, ImageView> data = new HashMap<>();
        LongStream.range(0, 10).forEach(num -> {
            ImageView img = new ImageView(getContext());
            squareUpView(img);
            img.setImageResource(R.drawable.pic);
            data.put(String.valueOf(num), img);
        });

        GridAdapter<ImageView> gridAdapter = new GridAdapter<>(getActivity(), data);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), COL_NUM);

        B.userImagesContainer.setLayoutManager(layoutManager);
        B.userImagesContainer.setAdapter(gridAdapter);

        /*
        Map<Long, ImageView> data = new HashMap<>();
        LongStream.range(0, 10).forEach(num -> {
            ImageView image = new ImageView(getContext());
            image.setImageResource(R.drawable.sent_msg);
            data.put(num, image);
        });*/
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    protected void fetchMediaPortion(Long portion_num)
    {
        mediaViewModel.fetchMediaPortion(Media.Type.IMAGE, portion_num, mediaList -> mediaList.forEach(this::fetchOneMedia));
    }
    @Override
    protected void observeMedia()
    {
        mediaViewModel.observeIncomingMedia(mediaActionModel -> {
            Media media = mediaActionModel.getModel();
            if (media.type.equals(Media.Type.IMAGE))
                fetchOneMedia(media);
        });
    }
    @Override
    protected void displayMedia(Media media, byte[] bytes) {
        ImageView imageView = new ImageView(getContext());
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        imageView.setImageBitmap(bitmap);
    }
}