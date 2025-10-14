package guide.kotlin.voipapp.utils

import android.content.Context
import android.util.Log
import guide.kotlin.voipapp.model.RegisterUserRequest
import guide.kotlin.voipapp.network.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun registerUserToBackend(context: Context, onRegister: () -> Unit) {
    val userDataStore = UserDataStore(context)
    val userId = userDataStore.getUserId()
    val phoneNumber = userDataStore.getPhoneNumber()
    val fcmToken = userDataStore.getFcmToken()

    if (userId == null || phoneNumber == null || fcmToken == null) {
        Log.e("RegistrationUtil", "Cannot register user, missing data.")
        return
    }

    val request = RegisterUserRequest(userId, phoneNumber, fcmToken)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = RetrofitInstance.api.registerUser(request)
            Log.d("RegistrationUtil", "User registration: ${response.message}")
            onRegister.invoke()
        } catch (e: Exception) {
            Log.e("RegistrationUtil", "Registration failed: ${e.message}")
        }
    }
}
