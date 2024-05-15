package mikhail.shell.gleamy.fragments;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;

import java.util.HashMap;

import mikhail.shell.gleamy.fragments.adapters.GridAdapter;

public abstract class GridMediaFragment<T extends View> extends UserMediaFragment<T>{
    protected final static int COL_NUM = 3;
    protected GridAdapter<T> gridAdapter;
    protected GridLayoutManager layoutManager;

    public GridMediaFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
    }

    @Override
    protected void initLayoutSettings()
    {
        gridAdapter = new GridAdapter<>(getActivity(), new HashMap<>());
        layoutManager = new GridLayoutManager(getActivity(), COL_NUM);
    }
    protected <T extends View> void squareUpView(T view)
    {
        int sideLength = 300; // B.userImagesContainer.getWidth() / COL_NUM;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(sideLength,sideLength);
        view.setLayoutParams(layoutParams);
    }

    @Override
    protected void removeOneMedia(String uuid) {
        gridAdapter.removeView(uuid);
    }
}
