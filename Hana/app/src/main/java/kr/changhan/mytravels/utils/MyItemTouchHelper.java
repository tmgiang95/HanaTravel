package kr.changhan.mytravels.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class MyItemTouchHelper extends ItemTouchHelper.Callback {

    private final MyItemTouchHelperListener mMyItemTouchHelperListener;

    public interface MyItemTouchHelperListener {
        boolean onRequestItemViewSwipeEnabled();
        void onItemDimiss(int position);
    }

    public MyItemTouchHelper(Context context) {
        if (context instanceof MyItemTouchHelperListener)
            mMyItemTouchHelperListener = (MyItemTouchHelperListener) context;
        else mMyItemTouchHelperListener = null;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        if (mMyItemTouchHelperListener != null)
            return mMyItemTouchHelperListener.onRequestItemViewSwipeEnabled();
        return false;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (mMyItemTouchHelperListener != null)
            mMyItemTouchHelperListener.onItemDimiss(viewHolder.getAdapterPosition());
    }
}
