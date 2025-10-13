package guide.kotlin.voipapp.ui



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import guide.kotlin.voipapp.model.CallState
import guide.kotlin.voipapp.viewmodel.CallViewModel

@Composable
fun MainScreen(viewModel: CallViewModel = viewModel()) {
    val status by viewModel.status.collectAsState()
    val callState by viewModel.callState.collectAsState()
    var userId by remember { mutableStateOf("") }
    var calleeId by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        when (callState) {
            CallState.Incoming -> IncomingCallUI(viewModel)
            CallState.Calling -> OutgoingCallUI(viewModel)
            CallState.Connected -> ActiveCallUI(viewModel)
            else -> RegistrationAndCallUI(viewModel, userId, calleeId, onUserChange = { userId = it }, onCalleeChange = { calleeId = it })
        }

        Text(
            text = "Status: $status",
            modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp)
        )
    }
}

@Composable
fun RegistrationAndCallUI(
    viewModel: CallViewModel,
    userId: String,
    calleeId: String,
    onUserChange: (String) -> Unit,
    onCalleeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = userId, onValueChange = onUserChange, label = { Text("Your User ID") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { viewModel.register(userId) }) { Text("Register") }
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = calleeId, onValueChange = onCalleeChange, label = { Text("Call User ID") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { viewModel.initiateCall(calleeId) }) { Text("Start Call") }
    }
}

@Composable
fun IncomingCallUI(viewModel: CallViewModel) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📞 Incoming Call", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { viewModel.answerCall() }) { Text("Accept") }
            Button(onClick = { viewModel.rejectCall() }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)) {
                Text("Reject")
            }
        }
    }
}

@Composable
fun OutgoingCallUI(viewModel: CallViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📞 Calling...", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.hangUp() }) { Text("Cancel") }
    }
}

@Composable
fun ActiveCallUI(viewModel: CallViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🗣️ In Call", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Button(onClick = { viewModel.hangUp() }) { Text("Hang Up") }
    }
}
