package at.fhv.mme.bt_dementia_app.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "activities",
    indices = [Index(
        value = ["name", "date", "time", "reminder_time", "reminder_audio_path", "additional_info", "amount", "unit", "is_done"],
        unique = true
    )]
)
data class Activity(
    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "date")
    val date: LocalDate,

    @ColumnInfo(name = "time")
    val time: LocalTime,

    @ColumnInfo(name = "reminder_time")
    val reminderTime: Int,

    @ColumnInfo(name = "reminder_audio_path")
    val reminderAudioPath: String,

    @ColumnInfo(name = "additional_info")
    val additionalInfo: String,

    @ColumnInfo(name = "amount")
    val amount: Int? = null,

    @ColumnInfo(name = "unit")
    val unit: String? = null,

    @ColumnInfo(name = "is_done")
    val isDone: Boolean = false,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long? = null
)