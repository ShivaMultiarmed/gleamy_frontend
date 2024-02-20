package mikhail.shell.gleamy.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mikhail.shell.gleamy.R;

public class UserImagesFragment extends Fragment {

    public UserImagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_images_fragment, container, false);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}