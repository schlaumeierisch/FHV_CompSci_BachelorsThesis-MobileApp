package at.fhv.mme.bt_dementia_app.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import at.fhv.mme.bt_dementia_app.model.Activity
import java.util.Calendar

object AlarmUtils {
    fun setupAlarm(activityId: Long, activity: Activity, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("activity_name", activity.name)
            putExtra("activity_info", activity.additionalInfo)
            putExtra("reminder_time", activity.reminderTime)
            putExtra("reminder_audio_path", activity.reminderAudioPath)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            activityId.toInt(),
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // set alarm time
        val calendar = Calendar.getInstance().apply {
            set(Calendar.YEAR, activity.date.year)
            set(Calendar.MONTH, activity.date.monthValue - 1)
            set(Calendar.DAY_OF_MONTH, activity.date.dayOfMonth)
            set(Calendar.HOUR_OF_DAY, activity.time.hour)
            set(Calendar.MINUTE, activity.time.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val reminderTimeInMillis = activity.reminderTime * 60000
        calendar.timeInMillis -= reminderTimeInMillis

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelAlarm(activity: Activity, requireActivity: FragmentActivity) {
        val alarmManager = requireActivity.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(requireActivity, AlarmReceiver::class.java).apply {
            putExtra("activity_name", activity.name)
            putExtra("activity_info", activity.additionalInfo)
            putExtra("reminder_time", activity.reminderTime)
            putExtra("reminder_audio_path", activity.reminderAudioPath)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            requireActivity,
            activity.id!!.toInt(),
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
    }
}