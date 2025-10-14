package guide.kotlin.voipapp.utils

import android.content.Context
import android.content.SharedPreferences

class UserDataStore(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_data", Context.MODE_PRIVATE)

    fun saveUser(userId: String, phoneNumber: String) {
        with(sharedPreferences.edit()) {
            putString("user_id", userId)
            putString("phone_number", phoneNumber)
            apply()
        }
    }

    fun getUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    fun getPhoneNumber(): String? {
        return sharedPreferences.getString("phone_number", null)
    }

    fun saveFcmToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("fcm_token", token)
            apply()
        }
    }

    fun getFcmToken(): String? {
        return sharedPreferences.getString("fcm_token", null)
    }
}