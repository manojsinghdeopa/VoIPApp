package guide.kotlin.voipapp.viewmodel

import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import guide.kotlin.voipapp.network.WebSocketManager
import guide.kotlin.voipapp.service.CallService
import guide.kotlin.voipapp.model.CallState
import guide.kotlin.voipapp.network.NetworkUtils.getServerWsUrl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject

class CallViewModel(app: Application) : AndroidViewModel(app), WebSocketManager.Listener {
    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _callState = MutableStateFlow(CallState.Idle)
    val callState: StateFlow<CallState> = _callState

    private val wsManager = WebSocketManager(this)
    private var currentCallId: String? = null


    fun connectToServer() {
        wsManager.connect(getServerWsUrl())
    }

    fun disConnectToServer() {
        wsManager.close()
    }

    override fun onCleared() {
        super.onCleared()
        disConnectToServer()
    }

    fun register(userId: String) {

        val msg = JSONObject().apply {
            put("type", "register")
            put("userId", userId)
        }
        wsManager.send(msg.toString())
    }

    fun initiateCall(to: String, from: String) {
        val msg = JSONObject().apply {
            put("type", "initiate_call")
            put("to", to)
            put("from", from)
        }
        wsManager.send(msg.toString())
        _callState.value = CallState.Calling
        _status.value = "Calling $to..."
    }

    fun answerCall() {
        currentCallId?.let {
            val msg = JSONObject().apply { put("type", "answer_call"); put("callId", it) }
            wsManager.send(msg.toString())
            _callState.value = CallState.Connected
            _status.value = "Call Connected"
        }
    }

    fun rejectCall() {
        currentCallId?.let {
            val msg = JSONObject().apply { put("type", "hangup"); put("callId", it) }
            wsManager.send(msg.toString())
        }
        _callState.value = CallState.Idle
        _status.value = "Call Rejected"
    }

    fun hangUp() {
        currentCallId?.let {
            val msg = JSONObject().apply { put("type", "hangup"); put("callId", it) }
            wsManager.send(msg.toString())
        }
        _callState.value = CallState.Ended
        _status.value = "Call Ended"
    }

    override fun onMessage(message: String) {
        val json = JSONObject(message)
        when (json.getString("type")) {
            "incoming_call" -> {
                currentCallId = json.getString("callId")
                val incomingFrom = json.optString("from")
                _callState.value = CallState.Incoming
                _status.value = "Incoming call from $incomingFrom"
                // start foreground call service to show full-screen notification
                val ctx = getApplication<Application>().applicationContext
                val intent = Intent(ctx, CallService::class.java).apply {
                    putExtra("callerName", incomingFrom)
                    putExtra("callId", currentCallId)
                }
                ContextCompat.startForegroundService(ctx, intent)
            }

            "call_initiated" -> {
                currentCallId = json.getString("callId")
                _status.value = "Call initiated"
            }

            "call_status" -> {
                val statusStr = json.getString("status")
                _status.value = statusStr
            }

            "call_connected" -> {
                _callState.value = CallState.Connected
                _status.value = "Connected"
            }

            "call_disconnected" -> {
                _callState.value = CallState.Ended
                _status.value = "Ended"
            }

            "call_failed" -> _status.value = "Call failed"
        }
    }

    override fun onConnected() {
        _status.value = "Connected to server"
    }

    override fun onDisconnected() {
        _status.value = "Disconnected"
    }

    override fun onFailure(error: String) {
        _status.value = "Error: $error"
    }

}
