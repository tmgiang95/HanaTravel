package kr.changhan.mytravels.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getSimpleName();

    private static final String TRAVEL_SORT_NAME = "TRAVEL_SORT_NAME";
    private static final String TRAVEL_SORT_KEY = "TRAVEL_SORT_KEY";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
    }

    /**
     * Returns the sorting option selected by the user from SharedPreferences.
     *
     * @return the selected sorting option.
     */
    public TravelSort getTravelSort() {
        SharedPreferences sharedPref =
                getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
        String name =
                sharedPref.getString(TRAVEL_SORT_KEY, TravelSort.DEFAULT.name());
        TravelSort travelSort = TravelSort.DEFAULT;
        try {
            travelSort = TravelSort.valueOf(name);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return travelSort;
    }


    /**
     * Saves the sorting option selected in SharedPreferences.
     *
     * @param travelSort the sorting option.
     */
    public void setTravelSort(TravelSort travelSort) {
        SharedPreferences sharedPref =
                getSharedPreferences(TRAVEL_SORT_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(TRAVEL_SORT_KEY, travelSort.name());
        editor.commit();
    }

}
