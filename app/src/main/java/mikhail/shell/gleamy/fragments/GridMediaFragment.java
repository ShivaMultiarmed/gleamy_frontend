package mikhail.shell.gleamy.fragments;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import mikhail.shell.gleamy.fragments.adapters.FragmentAdapter;
import mikhail.shell.gleamy.fragments.decorators.GridDecorator;

public abstract class GridMediaFragment<T extends View> extends MediaOverviewFragment<T>{
    protected final static int COL_NUM = 3, SPACING = 10;

    protected GridLayoutManager layoutManager;
    protected GridDecorator gridDecorator;

    public GridMediaFragment(Long userid, boolean isPrivileged) {
        super(userid, isPrivileged);
    }

    @Override
    protected void initLayoutSettings()
    {
        layoutManager = new GridLayoutManager(getActivity(), COL_NUM);
        gridDecorator = new GridDecorator(COL_NUM, SPACING, false);
    }
    protected RecyclerView getContainer()
    {
        return null;
    }
    @Override
    protected void removeMedia(String uuid) {
        fragmentAdapter.removeView(uuid);
    }
    @Override
    protected long getNextMediaPortionNumber()
    {
        return (long) Math.ceil(fragmentAdapter.getLoadedMediaCount() * 1.0 / MEDIA_PORTION) + 1L;
    }
}
