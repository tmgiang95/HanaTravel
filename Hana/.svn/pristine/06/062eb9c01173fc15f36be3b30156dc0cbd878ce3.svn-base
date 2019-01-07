package kr.changhan.mytravels.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;
import kr.changhan.mytravels.dao.TravelDao;
import kr.changhan.mytravels.entity.Travel;
import kr.changhan.mytravels.entity.TravelDiary;
import kr.changhan.mytravels.entity.TravelExpense;
import kr.changhan.mytravels.entity.TravelPlan;

@Database(entities = {Travel.class, TravelPlan.class, TravelDiary.class, TravelExpense.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    // refer to https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
        }
    };

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "travel_db")
                            .fallbackToDestructiveMigration()
                            /*.addMigrations(MIGRATION_1_2)*/
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract TravelDao travelDao();
}
