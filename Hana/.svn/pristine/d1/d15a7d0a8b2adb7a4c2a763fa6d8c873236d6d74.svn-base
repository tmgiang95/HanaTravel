package kr.changhan.mytravels.traveldetail.diary;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import kr.changhan.mytravels.R;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.traveldetail.DiaryDetailActivity;
import kr.changhan.mytravels.traveldetail.TravelDetailBaseFragment;

public class DiaryFragment extends TravelDetailBaseFragment {
    public static final int TITLE_ID = R.string.title_frag_dairy;
    private static final String TAG = DiaryFragment.class.getSimpleName();

    public DiaryFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DiaryFragment newInstance(int sectionNumber) {
        Log.d(TAG, "newInstance: sectionNumber=" + sectionNumber);
        DiaryFragment fragment = new DiaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_diary, container, false);
        return rootView;
    }

    @Override
    protected void onTravelChanged(Travel travel) {
    }

    @Override
    public void requestAddItem() {
        Intent intent = new Intent(getContext(), DiaryDetailActivity.class);
        startActivity(intent);
    }


}
