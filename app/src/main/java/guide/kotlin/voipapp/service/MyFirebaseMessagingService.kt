package guide.kotlin.voipapp.service


import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import guide.kotlin.voipapp.MainActivity
import guide.kotlin.voipapp.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.d("FCM", "token: $token")
        // TODO: send to backend via /register-device
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Msg: ${remoteMessage.data}")
        if (remoteMessage.data["type"] == "incoming_call") {
            val caller = remoteMessage.data["callerName"] ?: "Unknown"
            showIncomingCallNotification(caller, remoteMessage.data["callId"])
        }
    }

    private fun showIncomingCallNotification(caller: String, callId: String?) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("callerName", caller)
            putExtra("callId", callId)
        }
        val pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val ringtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val notification = NotificationCompat.Builder(this, "voip_calls_channel")
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("Incoming call")
            .setContentText("Call from $caller")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setFullScreenIntent(pending, true)
            .setSound(ringtone)
            .setAutoCancel(true)
            .build()

        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.createNotificationChannel(android.app.NotificationChannel("voip_calls_channel", "VoIP Calls", NotificationManager.IMPORTANCE_HIGH))
        nm.notify(1001, notification)
    }
}
