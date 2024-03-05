package at.fhv.mme.bt_dementia_app.utils

import android.content.res.Resources
import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object Converters {
    private val formatterDate = DateTimeFormatter.ISO_LOCAL_DATE
    private val formatterTime = DateTimeFormatter.ISO_LOCAL_TIME

    @TypeConverter
    @JvmStatic
    fun fromLocalDate(value: LocalDate?): String? {
        return value?.format(formatterDate)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalDate(value: String?): LocalDate? {
        return value?.let {
            LocalDate.parse(it, formatterDate)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromLocalTime(value: LocalTime?): String? {
        return value?.format(formatterTime)
    }

    @TypeConverter
    @JvmStatic
    fun toLocalTime(value: String?): LocalTime? {
        return value?.let {
            LocalTime.parse(it, formatterTime)
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromDayOfWeek(day: DayOfWeek): Int {
        return day.value
    }

    @TypeConverter
    @JvmStatic
    fun toDayOfWeek(day: Int): DayOfWeek {
        return DayOfWeek.of(day)
    }

    fun Int.dpToPx(): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

    fun Int.spToPx(): Int {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return (this * scale + 0.5f).toInt()
    }
}