package kr.changhan.mytravels.traveldetail.plan;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.entity.TravelPlan;
import kr.changhan.mytravels.main.TravelListItemClickListener;

public class TravelPlanListAdapter extends PagedListAdapter<TravelPlan, TravelPlanListAdapter.TravelViewHolder> {
    private final LayoutInflater mLayoutInflater;
    private TravelListItemClickListener mTravelListItemClickListener;
    public TravelPlanListAdapter(Context context) {
        super(TravelPlan.DIFF_CALLBACK);
        mLayoutInflater = LayoutInflater.from(context);
    }
    public void setListItemClickListener(TravelListItemClickListener travelListItemClickListener) {
        mTravelListItemClickListener = travelListItemClickListener;
    }
    @Override
    public void onBindViewHolder(@NonNull TravelViewHolder holder, int position) {
        TravelPlan item = getItem(position);
        if (item == null) {
            return;
        }
        holder.dateTxt.setText(item.getDateTimeMinText());
        holder.placeTxt.setText(item.getPlaceName());
        holder.titleTxt.setText(item.getTitle());
        holder.descTxt.setText(item.getDesc());
    }
    @NonNull
    @Override
    public TravelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mLayoutInflater.inflate(R.layout.fragment_plan_list_item, parent, false);
        return new TravelViewHolder(v);
    }
    @Override
    public void onCurrentListChanged(@Nullable PagedList<TravelPlan> previousList, @Nullable PagedList<TravelPlan> currentList) {
        super.onCurrentListChanged(previousList, currentList);
        notifyDataSetChanged();
    }
    class TravelViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTxt;
        private final TextView placeTxt;
        private final TextView titleTxt;
        private final TextView descTxt;
        private TravelViewHolder(View v) {
            super(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (descTxt.getMaxLines() == 3) {
                        descTxt.setMaxLines(Integer.MAX_VALUE);
                    } else {
                        descTxt.setMaxLines(3);
                    }
                    if (mTravelListItemClickListener != null) {


                        mTravelListItemClickListener.onListItemClick(v
                                , getAdapterPosition()

                                , getItem(getAdapterPosition())
                                , false);

                    }
                }
            });
            if (mTravelListItemClickListener != null) {
                v.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mTravelListItemClickListener.onListItemClick(v
                                , getAdapterPosition()

                                , getItem(getAdapterPosition())
                                , true);
                        return true;
                    }
                });
            }
            dateTxt = v.findViewById(R.id.date_txt);
            placeTxt = v.findViewById(R.id.place_txt);
            titleTxt = v.findViewById(R.id.title_txt);
            descTxt = v.findViewById(R.id.desc_txt);
        }
    }
    @Nullable
    @Override
    public TravelPlan getItem(int position) {
        return super.getItem(position);
    }
}