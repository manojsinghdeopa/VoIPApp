package guide.kotlin.voipapp.utils

import android.content.Context
import android.media.RingtoneManager
import android.os.VibrationEffect
import android.os.Vibrator
import android.media.Ringtone
import android.net.Uri

class CallAlertManager(private val context: Context) {
    private var ringtone: Ringtone? = null
    private var vibrator: Vibrator? = null

    fun startRinging(resUri: Uri? = null) {
        val uri = resUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        ringtone = RingtoneManager.getRingtone(context, uri)
        ringtone?.play()

        vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        val pattern = longArrayOf(0, 1000, 1000)
        val effect = VibrationEffect.createWaveform(pattern, 0)
        vibrator?.vibrate(effect)
    }

    fun stopRinging() {
        ringtone?.stop()
        vibrator?.cancel()
    }
}
