package mikhail.shell.gleamy.fragments.decorators;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GridDecorator extends RecyclerView.ItemDecoration {
    private final int spanCount, spacing;
    private final boolean hasSpaceAround;
    public GridDecorator(int spanCount, int spacing, boolean hasSpaceAround)
    {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.hasSpaceAround = hasSpaceAround;
    }
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column

        if (hasSpaceAround) {
            outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1 / spanCount) * spacing)
            outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1 / spanCount) * spacing)

            if (position < spanCount) { // top edge
                outRect.top = spacing;
            }
            outRect.bottom = spacing; // item bottom
        } else {
            outRect.left = column * spacing / spanCount; // column * ((1 / spanCount) * spacing)
            outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1 /    spanCount) * spacing)
            if (position >= spanCount) {
                outRect.top = spacing; // item top
            }
        }
    }
}
