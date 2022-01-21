package fr.ensisa.vallerich.comptidroid.ui.shared;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import fr.ensisa.vallerich.comptidroid.R;

public class ItemSwipeCallback extends ItemTouchHelper.SimpleCallback {

    public interface SwipeListener {
        void onSwiped (int direction, int position);
    }

    private SwipeListener listener;
    private Drawable iconDelete;
    private Drawable iconEdit;
    private final ColorDrawable backgroundDelete;
    private final ColorDrawable backgroundEdit;

    public ItemSwipeCallback(Context context, int directions, SwipeListener listener) {
        super(0, directions);
        if (listener == null) throw new Error("GestureHelper : SwipeListener is null");
        this.listener   = listener;
        iconDelete = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_delete);
        iconEdit = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_edit);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            backgroundDelete = new ColorDrawable(context.getResources().getColor(R.color.color_delete, null));
            backgroundEdit = new ColorDrawable(context.getResources().getColor(R.color.color_edit, null));
        } else {
            backgroundDelete = new ColorDrawable(context.getResources().getColor(R.color.color_delete));
            backgroundEdit = new ColorDrawable(context.getResources().getColor(R.color.color_edit));
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        View item = viewHolder.itemView;
        int backgroundCornerOffset = 20;
        if (dX > 0) {
            backgroundEdit.setBounds(
                    item.getLeft(), item.getTop(),
                    item.getLeft() + (int) dX + backgroundCornerOffset, item.getBottom()
            );
            backgroundEdit.draw(c);
            int iconMargin = (item.getHeight() - iconEdit.getIntrinsicHeight()) / 2;
            int iconTop = item.getTop() + iconMargin;
            int iconBottom = iconTop + iconEdit.getIntrinsicHeight();
            int iconLeft = item.getLeft() + iconMargin;
            int iconRight = item.getLeft() + iconMargin + iconEdit.getIntrinsicWidth();
            iconEdit.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            iconEdit.draw(c);
        } else if (dX < 0) {
            backgroundDelete.setBounds(
                    item.getRight() + (int) dX - backgroundCornerOffset, item.getTop(),
                    item.getRight(), item.getBottom()
            );
            backgroundDelete.draw(c);
            int iconMargin = (item.getHeight() - iconDelete.getIntrinsicHeight()) / 2;
            int iconTop = item.getTop() + iconMargin;
            int iconBottom = iconTop + iconDelete.getIntrinsicHeight();
            int iconLeft = item.getRight() - iconMargin - iconDelete.getIntrinsicWidth();
            int iconRight = item.getRight() - iconMargin;
            iconDelete.setBounds(iconLeft, iconTop, iconRight, iconBottom);
            iconDelete.draw(c);
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getLayoutPosition();
        listener.onSwiped(direction, position);
    }
}
