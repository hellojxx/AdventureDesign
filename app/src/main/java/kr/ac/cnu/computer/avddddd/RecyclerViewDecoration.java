package kr.ac.cnu.computer.avddddd;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;

public class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
    private final int divHeight;
    private final int divWidth;

    public RecyclerViewDecoration(int divHeight, int divWidth) {
        this.divHeight = divHeight;
        this.divWidth=divWidth;
    }

    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top=divHeight;
        outRect.right=divWidth;
        outRect.left=divWidth;
    }

}
