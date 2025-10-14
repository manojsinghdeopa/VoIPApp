package guide.kotlin.voipapp.network

import guide.kotlin.voipapp.model.CallLog
import guide.kotlin.voipapp.model.RegisterUserRequest
import guide.kotlin.voipapp.model.RegisterUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/call-logs/{userId}")
    suspend fun getCallLogs(@Path("userId") userId: String): List<CallLog>


    @POST("api/register-user")
    suspend fun registerUser(@Body body: RegisterUserRequest): RegisterUserResponse
}
