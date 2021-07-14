package io.ckl.fcmnotificationtester

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.random.Random

class NotificationHandler(val context: Context) {
    private val channelId: String = "01"

    /**
     * Create a notification channel with its title and settings as sound and
     * vibration pattern. A backend notification should have the channel ID so
     * it can be considered as part of this channel and trigger its default
     * notification settings (play the channel's sound, the vibration, etc.).
     * If a push notification is not part of any channel, by default Android
     * just put it on the notification center without pop-up or sound.
     **/
    fun createNotificationChannel(
        channelName: String = this.context.getString(
            R.string.general_channel_name
        ),
        channelDescription: String =  this.context.getString(
            R.string.general_channel_description
        ),
        channelId: String = this.channelId
    ) {
        // Create a notification channel in devices running most recent versions of Android
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = channelName
            val descriptionText = channelDescription
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // Set vibration and sound for the whole notification channel
            channel.setVibrationPattern(longArrayOf(0, 500));
            channel.enableVibration(true);
            channel.setSound(Settings.System.DEFAULT_NOTIFICATION_URI, null)

            with(NotificationManagerCompat.from(this.context)) {
                createNotificationChannel(channel)
            }
        }
    }

    /**
     * Create a notification inside the Android so it can show the pop-up with the data
     * instead of just getting the notification put inside the notification center.
     * If this function is not called from FCMService, no pop-up or sound/vibration will
     * be triggered, even if there is a channel and the notification data includes the
     * channel ID, when the app is in the foreground - and the "android_channel_id" is
     * not necessary in the received notification data.
     * However, if the app is in the background, it uses the "android_channel_id"
     * attribute coming from the FCM to link it to a channel and trigger the
     * pop-up and sound/vibration, based on the channel settings.
     **/
    fun sendNotification(
        title: String, body: String, channelId: String = this.channelId
    ) {
        // Create the notification builder
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.fcm_tester_notification_icon)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Send the notification to the device
        with(NotificationManagerCompat.from(this.context)) {
            notify(Random.nextInt(), builder.build())
        }
    }
}
