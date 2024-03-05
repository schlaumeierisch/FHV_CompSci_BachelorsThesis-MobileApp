package at.fhv.mme.bt_dementia_app.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import at.fhv.mme.bt_dementia_app.R
import at.fhv.mme.bt_dementia_app.view.MainActivity

class AlarmReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "ActivityAlarmChannel"

    override fun onReceive(context: Context, intent: Intent) {
        createNotificationChannel(context)

        if (ContextCompat.checkSelfPermission(
                context,
                "android.permission.POST_NOTIFICATIONS"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val reminderTime = intent.getIntExtra("reminder_time", 0)
        val reminderAudioPath = intent.getStringExtra("reminder_audio_path")
        val activityName = if (reminderTime == 0) {
            intent.getStringExtra("activity_name") + " (now)"
        } else {
            intent.getStringExtra("activity_name") + " (in $reminderTime minutes)"
        }
        val activityInfo = intent.getStringExtra("activity_info")

        // retrieve the resource identifier for the audio file
        val audioResId = context.resources.getIdentifier(
            reminderAudioPath,
            "raw",
            context.packageName
        )

        // create and start the MediaPlayer with the audio file
        MediaPlayerUtils.startMediaPlayer(context, audioResId)

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_alarm_24dp)
            .setContentTitle(activityName)
            .setContentText(activityInfo)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(1, notification)
    }

    private fun createNotificationChannel(context: Context) {
        val name = "ActivityAlarmChannel"
        val descriptionText = "Channel for activity alarm notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}