package guide.kotlin.voipapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = NavigationItem.Home.route) {
        composable(NavigationItem.Home.route) {
            MainScreen()
        }
        composable(NavigationItem.CallHistory.route) {
            CallHistoryScreen()
        }
    }
}
