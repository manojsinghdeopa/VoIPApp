package guide.kotlin.voipapp.service


import android.app.*
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import guide.kotlin.voipapp.MainActivity
import guide.kotlin.voipapp.R

class CallService : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val callerName = intent?.getStringExtra("callerName") ?: "Unknown"
        showIncomingCallNotification(callerName)
        return START_STICKY
    }

    private fun showIncomingCallNotification(callerName: String) {
        val channelId = "voip_calls_channel"
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val ch = NotificationChannel(channelId, "VoIP Calls", NotificationManager.IMPORTANCE_HIGH)
        ch.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        nm.createNotificationChannel(ch)

        val fullIntent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("callerName", callerName)
        }
        val fullPending = PendingIntent.getActivity(this, 0, fullIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val acceptIntent = Intent(this, CallReceiver::class.java).apply {
            action = "ACCEPT"
            putExtra("callerName", callerName)
        }
        val rejectIntent = Intent(this, CallReceiver::class.java).apply {
            action = "REJECT"
            putExtra("callerName", callerName)
        }
        val acceptPI = PendingIntent.getBroadcast(this, 1, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val rejectPI = PendingIntent.getBroadcast(this, 2, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_call)
            .setContentTitle("Incoming call")
            .setContentText("Call from $callerName")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(fullPending, true)
            .addAction(R.drawable.ic_accept, "Accept", acceptPI)
            .addAction(R.drawable.ic_reject, "Reject", rejectPI)
            .build()

        startForeground(1001, notification)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
