package io.ckl.fcmnotificationtester

import android.os.Handler
import android.os.Looper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService: FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Handle the message received from Firebase
        handleMessage(remoteMessage)
    }

    private fun handleMessage(remoteMessage: RemoteMessage) {
        val handler = Handler(Looper.getMainLooper())

        // Extract title and body to generate a notification
        val title = remoteMessage.notification?.title.toString()
        val body = remoteMessage.notification?.body.toString()

        handler.post(Runnable {
            // Call the method to generate and send the notification
            NotificationHandler(this).sendNotification(
                title=title, body=body
            )
        })

    }
}