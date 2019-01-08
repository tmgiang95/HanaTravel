package kr.changhan.mytravels.repository;
import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import kr.changhan.mytravels.dao.TravelDao;
import kr.changhan.mytravels.dao.TravelPlanDao;
import kr.changhan.mytravels.database.AppDatabase;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.entity.TravelPlan;
import kr.changhan.mytravels.utils.MyString;
public class TravelDetailRepository {
    private static volatile TravelDetailRepository INSTANCE;
    private final TravelDao mTravelDao;
    private final TravelPlanDao mTravelPlanDao;
    private TravelDetailRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTravelDao = db.travelDao();
        mTravelPlanDao = db.travelPlanDao();
    }
    public static TravelDetailRepository getInstance(final Application application) {
        if (INSTANCE == null) {
            synchronized (TravelDetailRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TravelDetailRepository(application);
                }
            }
        }
        return INSTANCE;
    }
    // travel
    public LiveData<Travel> getTravelById(long id) {

        return mTravelDao.getTravelById(id);
    }
    // plan
    public LiveData<PagedList<TravelPlan>> getAllPlansOfTravel(long travelId, String dateTime) {
        if (MyString.isEmpty(dateTime))
            return new LivePagedListBuilder<>(mTravelPlanDao.
                    getAllPlansOfTravel(travelId), 20).build();
        return new LivePagedListBuilder<>(mTravelPlanDao.getPlansOnDate(
                travelId, dateTime), 20).build();
    }
    public void insertTravelPlan(TravelPlan item) {
        new insertTravelPlanTask(mTravelPlanDao).execute(item);
    }
    public void updateTravelPlan(TravelPlan item) {
        new updateTravelPlanTask(mTravelPlanDao).execute(item);
    }
    public void deleteTravelPlan(TravelPlan item) {
        new deleteTravelPlanTask(mTravelPlanDao).execute(item);
    }
    private static class insertTravelPlanTask extends AsyncTask<
            TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;
        insertTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TravelPlan... params) {
            mAsyncTaskDao.insert(params);
            return null;
        }
    }



    private static class updateTravelPlanTask extends AsyncTask<TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;
        updateTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TravelPlan... params) {
            TravelPlan item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.undeleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.update(params);
            }
            return null;
        }
    }
    private static class deleteTravelPlanTask extends AsyncTask<TravelPlan, Void, Void> {
        private TravelPlanDao mAsyncTaskDao;
        deleteTravelPlanTask(TravelPlanDao dao) {
            mAsyncTaskDao = dao;
        }
        @Override
        protected Void doInBackground(final TravelPlan... params) {
            TravelPlan item = params[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.deleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.delete(params);
            }
            return null;
        }
    }
}
