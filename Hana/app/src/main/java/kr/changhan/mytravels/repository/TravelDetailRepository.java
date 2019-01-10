package kr.changhan.mytravels.repository;
import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.File;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import kr.changhan.mytravels.dao.TravelDao;
import kr.changhan.mytravels.dao.TravelDiaryDao;
import kr.changhan.mytravels.dao.TravelPlanDao;
import kr.changhan.mytravels.database.AppDatabase;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.entity.TravelDiary;
import kr.changhan.mytravels.entity.TravelPlan;
import kr.changhan.mytravels.utils.MyString;
public class TravelDetailRepository {
    private static volatile TravelDetailRepository INSTANCE;
    private final TravelDao mTravelDao;
    private final TravelPlanDao mTravelPlanDao;
    private final TravelDiaryDao mTravelDiaryDao;
    private TravelDetailRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTravelDao = db.travelDao();
        mTravelPlanDao = db.travelPlanDao();
        mTravelDiaryDao = db.travelDiaryDao();
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

    //diary
    public LiveData<PagedList<TravelDiary>> getAllDiariesOfTravel(long travelId, String dateTime) {
        if (MyString.isEmpty(dateTime))
            return new LivePagedListBuilder<>(mTravelDiaryDao.getAllDiariesOfTravel(travelId), 20).build();
        return new LivePagedListBuilder<>(mTravelDiaryDao.getDiariesOnDate(travelId, dateTime), 20).build();
    }

    public void insertTravelDiary(TravelDiary item) {
        new insertTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    public void updateTravelDiary(TravelDiary item) {
        new updateTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    public void deleteTravelDiary(TravelDiary item) {
        new deleteTravelDiaryTask(mTravelDiaryDao).execute(item);
    }

    private static class insertTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        insertTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(TravelDiary... travelDiaries) {
            mAsyncTaskDao.insert(travelDiaries);
            return null;
        }
    }

    private static class updateTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        updateTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }


        @Override
        protected Void doInBackground(TravelDiary... travelDiaries) {
            TravelDiary item = travelDiaries[0];
            if (item.getId() == -99) {
                mAsyncTaskDao.undeleteAllMarkedYes(item.getTravelId());
            } else {
                mAsyncTaskDao.update(travelDiaries);
            }
            return null;
        }
    }

    private static class deleteTravelDiaryTask extends AsyncTask<TravelDiary, Void, Void> {
        private TravelDiaryDao mAsyncTaskDao;

        deleteTravelDiaryTask(TravelDiaryDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(TravelDiary... travelDiaries) {
            TravelDiary item = travelDiaries[0];
            if (item.getId() == -99) {
                List<TravelDiary> list = mAsyncTaskDao.selectAllMarkedYes(item.getTravelId());
                for (TravelDiary diary : list) {
                    if (MyString.isNotEmpty(diary.getImgUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getImgUri()).getPath()));
                            file.delete();
                            //delete image belongs to item
                        } catch (Exception e) {
                        }
                    }
                    if (MyString.isNotEmpty(diary.getThumbUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getThumbUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                        }
                    }
                }
                mAsyncTaskDao.deleteAllMarkedYes(item.getTravelId());
            } else {
                for (TravelDiary diary : travelDiaries) {
                    if (MyString.isNotEmpty(diary.getImgUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getImgUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                        }
                    }
                    if (MyString.isNotEmpty(diary.getThumbUri())) {
                        try {
                            File file = new File((Uri.parse(diary.getThumbUri()).getPath()));
                            file.delete();
                        } catch (Exception e) {
                        }
                    }
                }
                mAsyncTaskDao.delete(travelDiaries);
            }
            return null;
        }
    }
}
