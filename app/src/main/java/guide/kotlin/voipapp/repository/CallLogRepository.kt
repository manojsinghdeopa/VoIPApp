package guide.kotlin.voipapp.repository

import guide.kotlin.voipapp.model.CallLog
import guide.kotlin.voipapp.network.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CallLogRepository(private val api: ApiService) {

    private val callLogs = MutableStateFlow<List<CallLog>>(emptyList())

    fun getLocalLogs(): Flow<List<CallLog>> {
        return callLogs
    }

    suspend fun syncFromServer(userId: String) {
        try {
            val logs = api.getCallLogs(userId)
            callLogs.value = logs
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}