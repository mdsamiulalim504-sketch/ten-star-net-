package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.AuthState
import com.example.ui.IspViewModel
import com.example.ui.screens.AdminDashboardScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.UserDashboardScreen
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {

    private val viewModel: IspViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DeepNavy
                ) {
                    TenStarApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun TenStarApp(viewModel: IspViewModel) {
    val authState by viewModel.authState.collectAsState()
    val loginError by viewModel.loginError.collectAsState()

    AnimatedContent(
        targetState = authState,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "ScreenTransition"
    ) { state ->
        when (state) {
            is AuthState.LoggedOut -> {
                LoginScreen(
                    onLoginCustomer = { u, p -> viewModel.loginCustomer(u, p) },
                    onLoginAdmin = { u, p -> viewModel.loginAdmin(u, p) },
                    loginError = loginError
                )
            }
            is AuthState.CustomerLoggedIn -> {
                UserDashboardScreen(
                    user = state.user,
                    viewModel = viewModel,
                    onLogout = { viewModel.logout() }
                )
            }
            is AuthState.AdminLoggedIn -> {
                AdminDashboardScreen(
                    viewModel = viewModel,
                    onLogout = { viewModel.logout() }
                )
            }
        }
    }
}
