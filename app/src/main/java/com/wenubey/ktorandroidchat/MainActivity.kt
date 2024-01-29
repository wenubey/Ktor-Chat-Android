package com.wenubey.ktorandroidchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wenubey.ktorandroidchat.ui.chat.ChatScreen
import com.wenubey.ktorandroidchat.ui.theme.KtorAndroidChatTheme
import com.wenubey.ktorandroidchat.ui.username.UsernameScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            KtorAndroidChatTheme {
                navController = rememberNavController()
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "usernameScreen"
                    ) {
                        composable("usernameScreen") {
                            UsernameScreen(onNavigate = navController::navigate)
                        }
                        composable("chatScreen/{username}",
                            arguments = listOf(
                                navArgument(name = "username") {
                                    type = NavType.StringType
                                    nullable = true
                                }
                            )
                        ) {
                            val username = it.arguments?.getString("username")
                            ChatScreen(username = username)
                        }
                    }
                }
            }
        }
    }
}
