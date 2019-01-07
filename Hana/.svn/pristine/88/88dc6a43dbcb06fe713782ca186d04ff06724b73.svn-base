package kr.changhan.mytravels.main;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import kr.changhan.mytravels.EditTravelActivity;
import kr.changhan.mytravels.MainActivity;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.base.MyConst;
import kr.changhan.mytravels.entity.Travel;

public class TravelListAdapter extends RecyclerView.Adapter<TravelListAdapter.TravelViewHolder> {

    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private List<Travel> mList;
    private ListItemClickListener mListItemClickListener;

    public TravelListAdapter(Context context) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setListItemClickListener(ListItemClickListener listItemClickListener) {
        mListItemClickListener = listItemClickListener;
    }

    @Override
    public int getItemCount() {
        if (mList == null) return 0;
        return mList.size();
    }

    public void setList(List<Travel> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        if (getItemCount() == 0) return RecyclerView.NO_ID;
        return mList.get(position).getId();
    }

    private Travel getItem(int position) {
        if (getItemCount() == 0) return null;
        return mList.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        if (getItemCount() == 0) return;
        Travel item = mList.get(position);
        holder.titleTxt.setText(item.getTitle());
        holder.placeTxt.setText(item.getPlaceName() + " / " + item.getPlaceAddr());
        holder.dateTxt.setText(item.getDateTimeText() + " ~ " + item.getEndDtText());
    }

    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.main_travel_item, parent, false);
        return new TravelViewHolder(v);
    }

    /**
     * Interface definition for the callback to call when the list item is clicked.
     */
    public interface ListItemClickListener {
        /**
         * Called when the list item been clicked.
         *
         * @param view     The view that was clicked.
         * @param position The position that was clicked.
         * @param travel   The travel object that was clicked.
         */
        void onListItemClick(View view, int position, Travel travel);
    }

    class TravelViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTxt;
        private final TextView placeTxt;
        private final TextView dateTxt;
        private final ImageButton editBtn;

        private TravelViewHolder(View v) {
            super(v);
            if (mListItemClickListener != null) {
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListItemClickListener.onListItemClick(v
                                , getAdapterPosition()
                                , getItem(getAdapterPosition()));
                    }
                });
            }
            titleTxt = v.findViewById(R.id.title_txt);
            placeTxt = v.findViewById(R.id.place_txt);
            dateTxt = v.findViewById(R.id.date_txt);
            editBtn = v.findViewById(R.id.editBtn);
            editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // call EditTravelActivity with a selected Travel entity.
                    Travel travel = getItem(getAdapterPosition());
                    Intent intent = new Intent(mContext, EditTravelActivity.class);
                    intent.putExtra(MyConst.REQKEY_TRAVEL, travel);
                    intent.setAction(MyConst.REQACTION_EDIT_TRAVEL);
                    ((MainActivity) mContext).startActivityForResult(intent
                            , MyConst.REQCD_TRAVEL_EDIT);
                }
            });
        }
    }
}
