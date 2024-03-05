package at.fhv.mme.bt_dementia_app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import at.fhv.mme.bt_dementia_app.model.Activity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities WHERE `date` = :date ORDER BY `time`")
    fun getAllActivitiesByDate(date: LocalDate): Flow<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addActivity(activity: Activity): Long

    @Update
    fun updateActivity(activity: Activity)

    @Delete
    fun deleteActivity(activity: Activity)
}