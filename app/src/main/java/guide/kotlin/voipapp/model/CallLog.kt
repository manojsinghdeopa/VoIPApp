package guide.kotlin.voipapp.model

data class CallLog(
    val call_id: String,
    val twilio_sid: String,
    val direction: String,
    val from_user: String,
    val to_user: String,
    val status: String,
    val started_at: String,
    val ended_at: String?
)