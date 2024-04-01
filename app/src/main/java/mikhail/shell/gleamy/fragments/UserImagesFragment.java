package mikhail.shell.gleamy.fragments;

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

public class UserImagesFragment extends Fragment {
    private UserImagesFragmentBinding B;
    private final static int COL_NUM = 3;
    public UserImagesFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        B = UserImagesFragmentBinding.inflate(inflater, container, false);
        return B.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //B.userImagesContainer.measure(0,0);

        Map<Long, ImageView> data = new HashMap<>();
        LongStream.range(0, 10).forEach(num -> {
            ImageView img = new ImageView(getContext());
            adjustSize(img);
            img.setImageResource(R.drawable.pic);
            data.put(num, img);
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
    private <T extends View> void adjustSize(T view)
    {
        int sideLength = 300; // B.userImagesContainer.getWidth() / COL_NUM;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(sideLength,sideLength);
        view.setLayoutParams(layoutParams);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}