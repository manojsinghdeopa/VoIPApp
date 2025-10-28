package guide.kotlin.voipapp.ui


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import guide.kotlin.voipapp.model.CallState
import guide.kotlin.voipapp.utils.UserDataStore
import guide.kotlin.voipapp.utils.registerUserToBackend
import guide.kotlin.voipapp.viewmodel.CallViewModel

@Composable
fun MainScreen(viewModel: CallViewModel = viewModel()) {
    val context = LocalContext.current
    val userDataStore = UserDataStore(context)
    var isLoggedIn by remember { mutableStateOf(userDataStore.getUserId() != null) }

    if (isLoggedIn) {
        LaunchedEffect(Unit) {
            viewModel.connectToServer()
            viewModel.register(userId = userDataStore.getUserId().toString())
        }

        val status by viewModel.status.collectAsState()
        val callState by viewModel.callState.collectAsState()
        var calleeId by remember { mutableStateOf("") }

        Box(modifier = Modifier.fillMaxSize()) {
            when (callState) {
                CallState.Incoming -> IncomingCallUI(viewModel)
                CallState.Calling -> OutgoingCallUI(viewModel)
                CallState.Connected -> ActiveCallUI(viewModel)
                else -> CallUI(viewModel, calleeId, userDataStore.getPhoneNumber().toString(), onCalleeChange = { calleeId = it })
            }

            Text(
                text = status,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    } else {
        RegistrationUI(onRegister = {
            isLoggedIn = true
        })
    }
}

@Composable
fun RegistrationUI(onRegister: () -> Unit) {
    val context = LocalContext.current
    val userDataStore = UserDataStore(context)
    var userId by rememberSaveable { mutableStateOf("") }
    var phoneNumber by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(value = userId, onValueChange = { userId = it }, label = { Text("User Name") })
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = phoneNumber, onValueChange = { phoneNumber = it }, label = { Text("Phone Number") })
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            userDataStore.saveUser(userId, phoneNumber)
            registerUserToBackend(context, onRegister)
        }) { Text("Register") }
    }
}

@Composable
fun CallUI(
    viewModel: CallViewModel,
    calleeId: String,
    callerId: String,
    onCalleeChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = calleeId, onValueChange = onCalleeChange, label = { Text("User Number") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = { viewModel.initiateCall(calleeId, callerId) }) { Text("Start Call") }
    }
}

@Composable
fun IncomingCallUI(viewModel: CallViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
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
