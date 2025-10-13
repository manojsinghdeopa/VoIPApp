package guide.kotlin.voipapp.network

import guide.kotlin.voipapp.model.CallLog
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("api/call-logs/{userId}")
    suspend fun getCallLogs(@Path("userId") userId: String): List<CallLog>
}
