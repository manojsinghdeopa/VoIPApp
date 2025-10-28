package guide.kotlin.voipapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guide.kotlin.voipapp.repository.CallLogRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CallHistoryViewModel(private val repo: CallLogRepository) : ViewModel() {

    val callLogs = repo.getLocalLogs().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )

    fun syncCallLogs(userId: String) {
        viewModelScope.launch { repo.syncFromServer(userId) }
    }
}
