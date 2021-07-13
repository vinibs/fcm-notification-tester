package io.ckl.fcmnotificationtester

import android.os.Bundle
import android.provider.Settings.Secure
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private var textViewTokenValue: TextView? = null
    private var textViewIdValue: TextView? = null
    private var deviceId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.textViewTokenValue = findViewById(R.id.tokenValue)
        this.textViewIdValue = findViewById(R.id.idValue)

        Toast.makeText(
            baseContext,
            getString(R.string.copy_toast_message),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onStart() {
        super.onStart()

        // Triggers the creation of the notification channel
        NotificationHandler(this).createNotificationChannel()

        this.deviceId = Secure.getString(contentResolver, Secure.ANDROID_ID)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result.toString()

            this.loadScreenData(token=token)
        })
    }

    fun loadScreenData(token: String) {
        // Put the token on the screen
        this.textViewTokenValue?.text = token
        Log.d("FCMToken", token)

        // Put the device ID on the screen
        this.textViewIdValue?.text = this.deviceId
        Log.d("DeviceId", this.deviceId.toString())

    }
}