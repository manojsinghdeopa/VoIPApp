package guide.kotlin.voipapp.network

import android.os.Build

object NetworkUtils {

    // Localhost URL configuration for emulator + real device.
    private const val SERVER_PORT = 8080
    private const val SIGNALING_SERVER_PORT = 8081

    // replace your local IP (same wifi/network Mobile+Laptop)
    // https://chatgpt.com/c/68ece585-ef0c-8321-8564-962b6a15078d
    private val baseIp: String by lazy { if (isEmulator()) "10.0.2.2" else "192.168.1.14" }

    fun getServerHttpUrl(): String = "http://$baseIp:$SERVER_PORT/"

    fun getServerWsUrl(): String = "ws://$baseIp:$SIGNALING_SERVER_PORT"

    private fun isEmulator(): Boolean = (Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.lowercase().contains("vbox")
            || Build.FINGERPRINT.lowercase().contains("test-keys")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            || "google_sdk" == Build.PRODUCT)
}
