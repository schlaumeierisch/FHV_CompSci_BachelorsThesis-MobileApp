package at.fhv.mme.bt_dementia_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(
    tableName = "medication",
    indices = [Index(value = ["name", "day_of_week", "time", "amount", "unit"], unique = true)]
)
data class Medication(

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: DayOfWeek,

    @ColumnInfo(name = "time")
    val time: LocalTime,

    @ColumnInfo(name = "amount")
    val amount: Int,

    @ColumnInfo(name = "unit")
    val unit: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null
)