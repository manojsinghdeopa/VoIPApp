package guide.kotlin.voipapp.network

import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit

class WebSocketManager(private val listener: Listener) {
    interface Listener {
        fun onMessage(message: String)
        fun onConnected()
        fun onDisconnected()
        fun onFailure(error: String)
    }

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient.Builder()
        .readTimeout(0, TimeUnit.MILLISECONDS)
        .build()

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                listener.onConnected()
            }

            override fun onMessage(ws: WebSocket, text: String) {
                listener.onMessage(text)
            }

            override fun onMessage(ws: WebSocket, bytes: ByteString) {
                listener.onMessage(bytes.utf8())
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                listener.onDisconnected()
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                listener.onFailure(t.message ?: "unknown")
            }
        })
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun close() {
        webSocket?.close(1000, "Client closing")
    }
}
