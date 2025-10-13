package guide.kotlin.voipapp.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import guide.kotlin.voipapp.network.ApiService
import guide.kotlin.voipapp.repository.CallLogRepository
import guide.kotlin.voipapp.viewmodel.CallHistoryViewModel

class CallHistoryViewModelFactory(private val api: ApiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CallHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CallHistoryViewModel(CallLogRepository(api)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}