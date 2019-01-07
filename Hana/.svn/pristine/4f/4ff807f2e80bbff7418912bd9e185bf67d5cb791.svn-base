package kr.changhan.mytravels.repository;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;
import kr.changhan.mytravels.base.TravelSort;
import kr.changhan.mytravels.dao.TravelDao;
import kr.changhan.mytravels.database.AppDatabase;
import kr.changhan.mytravels.entity.Travel;

public class TravelRepository {
    private static volatile TravelRepository INSTANCE;
    private final TravelDao mTravelDao;

    /*
    private LiveData<List<Travel>> mAllTravels;
    public LiveData<List<Travel>> getAllTravels() {
        if (mAllTravels == null) {
            mAllTravels = mTravelDao.getAllTravels();
        }
        return mAllTravels;
    }
    */

    private TravelRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mTravelDao = db.travelDao();
    }

    public static TravelRepository getInstance(final Application application) {
        if (INSTANCE == null) {
            synchronized (TravelRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new TravelRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    public LiveData<List<Travel>> getAllTravels(TravelSort travelSort) {
        switch (travelSort) {
            case DEFAULT:
                return mTravelDao.getAllTravels();
            case TITLE_ASC:
                return mTravelDao.getAllTravelsByTitleAsc();
            case TITLE_DESC:
                return mTravelDao.getAllTravelsByTitleDesc();
            case START_ASC:
                return mTravelDao.getAllTravelsByStartAsc();
            case START_DESC:
                return mTravelDao.getAllTravelsByStartDesc();
        }
        return mTravelDao.getAllTravels();
    }

    public void insert(Travel travel) {
        new insertAsyncTask(mTravelDao).execute(travel);
    }

    public void update(Travel travel) {
        new updateAsyncTask(mTravelDao).execute(travel);
    }

    public void delete(Travel... travels) {
        new deleteAsyncTask(mTravelDao).execute(travels);
    }

    private static class insertAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        insertAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        updateAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            mAsyncTaskDao.update(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Travel, Void, Void> {
        private TravelDao mAsyncTaskDao;

        deleteAsyncTask(TravelDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Travel... params) {
            mAsyncTaskDao.delete(params);
            return null;
        }
    }
}
