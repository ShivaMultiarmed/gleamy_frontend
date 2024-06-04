package mikhail.shell.gleamy.fragments;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import mikhail.shell.gleamy.models.Media;

public abstract class LinearMediaFragment<T extends View> extends MediaOverviewFragment<T>{
    protected final static int SPACING = 10;

    public LinearMediaFragment(Long userid, Media.Type mediaType, boolean isPrivileged) {
        super(userid, mediaType, isPrivileged);
    }

    @Override
    protected void initLayoutSettings()
    {
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        super.initLayoutSettings();
    }
}
