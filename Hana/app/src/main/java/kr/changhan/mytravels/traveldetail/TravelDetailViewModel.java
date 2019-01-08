package kr.changhan.mytravels.traveldetail;

import android.app.Application;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.PagedList;
import kr.changhan.mytravels.base.MyConst;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.entity.TravelDiary;
import kr.changhan.mytravels.entity.TravelExpense;
import kr.changhan.mytravels.entity.TravelPlan;
import kr.changhan.mytravels.repository.TravelDetailRepository;

public class TravelDetailViewModel extends AndroidViewModel {
    private final TravelDetailRepository mRepository;
    public TravelDetailViewModel(@NonNull Application application) {
        super(application);
        mRepository = TravelDetailRepository.getInstance(application);
    }
    // travel
    private final MutableLiveData<Long> mTravelId = new MutableLiveData<>
            ();
    private final LiveData<Travel> mTravel = Transformations.switchMap(
            mTravelId, new Function<Long, LiveData<Travel>>() {
                @Override
                public LiveData<Travel> apply(Long id) {
                    return mRepository.getTravelById(id);
                }
            });

    public LiveData<Long> getTravelId() {
        return mTravelId;
    }
    public void setTravelId(long id) {
        mTravelId.setValue(id);
    }
    public LiveData<Travel> getTravel() {
        return mTravel;
    }
    // plan
    private final MutableLiveData<Map<String, Object>>
            mTravelPlanListOption = new MutableLiveData<>();
    private final LiveData<PagedList<TravelPlan>> mTravelPlanList
            = Transformations.switchMap(mTravelPlanListOption, new
            Function<Map<String, Object>, LiveData<PagedList<TravelPlan>>>() {
                @Override
                public LiveData<PagedList<TravelPlan>> apply(Map<String, Object>
                                                                     option) {
                    return mRepository.getAllPlansOfTravel((long) option.get(
                            MyConst.KEY_ID), null);
                }
            });
    public void setTravelPlanListOption(Map<String, Object> option) {
        if (option == null) {
            option = new HashMap<>();
            option.put(MyConst.KEY_ID, 0);
        }
        mTravelPlanListOption.setValue(option);
    }
    public LiveData<PagedList<TravelPlan>> getTravelPlanList() {
        return mTravelPlanList;
    }
}
