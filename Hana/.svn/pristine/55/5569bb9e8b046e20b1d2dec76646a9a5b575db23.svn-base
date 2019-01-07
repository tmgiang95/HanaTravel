package kr.changhan.mytravels.traveldetail;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import kr.changhan.mytravels.entity.Travel;

public abstract class TravelDetailBaseFragment extends Fragment {
    protected static final String ARG_SECTION_NUMBER = "section_number";
    private final static String TAG = TravelDetailBaseFragment.class.getSimpleName();

    protected abstract void onTravelChanged(Travel travel);

    public abstract void requestAddItem();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
