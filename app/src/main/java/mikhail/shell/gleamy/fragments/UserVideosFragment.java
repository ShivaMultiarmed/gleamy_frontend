package mikhail.shell.gleamy.fragments;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import mikhail.shell.gleamy.R;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.views.Tab;

public class UserVideosFragment extends GridMediaFragment<VideoView> {

    public UserVideosFragment() {
        super();
        MEDIA_TYPE = Media.Type.VIDEO;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_videos, container, false);
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    protected void displayMedia(Media media, byte[] bytes) {
        // TO DO
    }
}