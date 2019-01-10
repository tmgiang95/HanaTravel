package kr.changhan.mytravels.dao;

import java.util.List;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import kr.changhan.mytravels.entity.TravelDiary;

@Dao
public interface TravelDiaryDao {

    @Query("SELECT * from travel_diary WHERE deleteYn=0 and travelId=:travelId ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelDiary> getAllDiariesOfTravel(long travelId);

    @Query("SELECT * from travel_diary WHERE deleteYn=0 and travelId=:travelId and substr(dateTime, 1, 8) = substr(:dateTime, 1, 8) ORDER BY dateTime DESC, id DESC")
    DataSource.Factory<Integer, TravelDiary> getDiariesOnDate(long travelId, String dateTime);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TravelDiary... items);
    @Update
    void update(TravelDiary... items);
    @Delete
    void delete(TravelDiary... items);

    @Query("SELECT * FROM travel_diary WHERE deleteYn=1 and travelId=:travelId")
    List<TravelDiary> selectAllMarkedYes(long travelId);

    @Query("DELETE FROM travel_diary WHERE deleteYn=1 and travelId=:travelId")
    void deleteAllMarkedYes(long travelId);
    @Query("UPDATE travel_diary SET deleteYn=0 WHERE deleteYn=1 and travelId=:travelId")
    void undeleteAllMarkedYes(long travelId);
}
