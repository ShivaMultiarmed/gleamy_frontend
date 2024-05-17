package mikhail.shell.gleamy.fragments;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

import mikhail.shell.gleamy.fragments.adapters.GridAdapter;
import mikhail.shell.gleamy.fragments.decorators.GridDecorator;

public abstract class GridMediaFragment<T extends View> extends UserMediaFragment<T>{
    protected final static int COL_NUM = 3, SPACING = 10;
    protected GridAdapter<T> gridAdapter;
    protected GridLayoutManager layoutManager;
    protected GridDecorator gridDecorator;

    public GridMediaFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
    }

    @Override
    protected void initLayoutSettings()
    {
        gridAdapter = new GridAdapter<>(getActivity(), new HashMap<>());
        layoutManager = new GridLayoutManager(getActivity(), COL_NUM);
        gridDecorator = new GridDecorator(COL_NUM, SPACING, false);
    }
    protected <T extends View> void squareUpView(T view)
    {
        int sideLength = (getContainer().getLayoutManager().getWidth() - (COL_NUM-1)*SPACING) / COL_NUM;
        ViewGroup.LayoutParams layoutParams =
                new ViewGroup.LayoutParams(sideLength,sideLength+500);
        view.setLayoutParams(layoutParams);
    }
    protected RecyclerView getContainer()
    {
        return null;
    }

    @Override
    protected void removeOneMedia(String uuid) {
        gridAdapter.removeView(uuid);
    }
}
