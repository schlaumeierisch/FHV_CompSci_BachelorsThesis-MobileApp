package at.fhv.mme.bt_dementia_app.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.fhv.mme.bt_dementia_app.model.Medication
import kotlinx.coroutines.flow.Flow
import java.time.DayOfWeek

@Dao
interface MedicationDao {
    @Query("SELECT * FROM medication ORDER BY `name`")
    fun getAllMedication(): Flow<List<Medication>>

    @Query("SELECT * FROM medication WHERE `day_of_week` = :day ORDER BY `time`")
    fun getAllMedicationByDay(day: DayOfWeek): Flow<List<Medication>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addMedication(medication: Medication)

    @Delete
    fun deleteMedication(medication: Medication)
}