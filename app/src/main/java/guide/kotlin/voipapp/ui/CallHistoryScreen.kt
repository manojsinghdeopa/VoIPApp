package guide.kotlin.voipapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import guide.kotlin.voipapp.factory.CallHistoryViewModelFactory
import guide.kotlin.voipapp.network.RetrofitInstance
import guide.kotlin.voipapp.utils.UserDataStore
import guide.kotlin.voipapp.viewmodel.CallHistoryViewModel

@Composable
fun CallHistoryScreen(viewModel: CallHistoryViewModel = viewModel(factory = CallHistoryViewModelFactory(RetrofitInstance.api))) {

    val context = LocalContext.current
    val userDataStore = UserDataStore(context)
    val logs = viewModel.callLogs.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.syncCallLogs(userDataStore.getPhoneNumber().toString())
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Text("📞 Call History", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(logs.value) { log ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("${log.direction.uppercase()} • ${log.status}")
                        Text("From: ${log.from_user}")
                        Text("To: ${log.to_user}")
                        Text("Started: ${log.started_at}")
                        if (log.ended_at != null) Text("Ended: ${log.ended_at}")
                    }
                }
            }
        }
    }
}
