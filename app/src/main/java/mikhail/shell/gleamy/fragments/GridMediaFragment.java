package mikhail.shell.gleamy.fragments;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;

import mikhail.shell.gleamy.fragments.decorators.GridDecorator;
import mikhail.shell.gleamy.models.Media;
import mikhail.shell.gleamy.models.Media.Type;

public abstract class GridMediaFragment<T extends View> extends MediaOverviewFragment<T>{
    protected final static int COL_NUM = 3, SPACING = 10;

    public GridMediaFragment(Long userid, Type mediaType, boolean isPrivileged) {
        super(userid, mediaType, isPrivileged);
    }

    @Override
    protected void initLayoutSettings()
    {
        layoutManager = new GridLayoutManager(getActivity(), COL_NUM);
        super.initLayoutSettings();
        final ItemDecoration decorator = new GridDecorator(COL_NUM, SPACING, false);
        container.addItemDecoration(decorator);
    }
}
