package guide.kotlin.voipapp.service


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import guide.kotlin.voipapp.MainActivity

class CallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        val caller = intent?.getStringExtra("callerName")
        Log.d("CallReceiver", "Action $action, from $caller")
        when (action) {
            "ACCEPT" -> {
                val launch = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    putExtra("acceptCall", true)
                    putExtra("callerName", caller)
                }
                context?.startActivity(launch)
            }

            "REJECT" -> {
                // stop service and send hangup to backend via ViewModel or local small HTTP call
                val stop = Intent(context, CallService::class.java)
                context?.stopService(stop)
            }
        }
    }
}
