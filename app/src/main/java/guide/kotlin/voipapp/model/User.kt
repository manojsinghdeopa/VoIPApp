package guide.kotlin.voipapp.model

data class RegisterUserRequest(
    val userId: String,
    val phoneNumber: String,
    val fcmToken: String
)

data class RegisterUserResponse(
    val success: Boolean,
    val message: String
)