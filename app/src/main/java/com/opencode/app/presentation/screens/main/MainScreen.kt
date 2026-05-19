package com.opencode.app.presentation.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.opencode.app.presentation.screens.chat.ChatScreen
import com.opencode.app.presentation.screens.sessions.SessionsScreen
import com.opencode.app.presentation.screens.settings.SettingsScreen
import com.opencode.app.presentation.screens.explorer.FileExplorerScreen

/**
 * 主屏幕，包含底部导航栏
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MainViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(getScreenTitle(currentRoute)) },
                actions = {
                    // 连接状态指示器
                    ConnectionStatusIndicator(
                        isConnected = uiState.isConnected,
                        serverVersion = uiState.serverVersion
                    )
                    IconButton(onClick = { /* 打开通知 */ }) {
                        BadgedBox(
                            badge = {
                                if (uiState.pendingQuestions > 0) {
                                    Badge { Text(uiState.pendingQuestions.toString()) }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "通知")
                        }
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Chat, contentDescription = "对话") },
                    label = { Text("对话") },
                    selected = currentRoute == Screen.Chat.route,
                    onClick = { navController.navigate(Screen.Chat.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "会话") },
                    label = { Text("会话") },
                    selected = currentRoute == Screen.Sessions.route,
                    onClick = { navController.navigate(Screen.Sessions.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Folder, contentDescription = "文件") },
                    label = { Text("文件") },
                    selected = currentRoute == Screen.Explorer.route,
                    onClick = { navController.navigate(Screen.Explorer.route) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "设置") },
                    label = { Text("设置") },
                    selected = currentRoute == Screen.Settings.route,
                    onClick = { navController.navigate(Screen.Settings.route) }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Chat.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Chat.route) {
                ChatScreen(
                    onNavigateToSession = { sessionId ->
                        navController.navigate("chat/$sessionId")
                    }
                )
            }
            composable(Screen.Sessions.route) {
                SessionsScreen(
                    onSessionClick = { sessionId ->
                        navController.navigate("chat/$sessionId")
                    }
                )
            }
            composable(Screen.Explorer.route) {
                FileExplorerScreen()
            }
            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }
}

@Composable
private fun ConnectionStatusIndicator(
    isConnected: Boolean,
    serverVersion: String
) {
    Row(
        modifier = Modifier.padding(end = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // 连接状态点
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(if (isConnected) Color(0xFF4CAF50) else Color(0xFFF44336))
        )
        Text(
            text = if (isConnected) "已连接" else "未连接",
            style = MaterialTheme.typography.bodySmall
        )
    }
}

private fun getScreenTitle(route: String?): String {
    return when (route) {
        Screen.Chat.route -> "OpenCode 对话"
        Screen.Sessions.route -> "会话列表"
        Screen.Explorer.route -> "文件浏览器"
        Screen.Settings.route -> "设置"
        else -> "OpenCode"
    }
}

sealed class Screen(val route: String) {
    object Chat : Screen("chat")
    object Sessions : Screen("sessions")
    object Explorer : Screen("explorer")
    object Settings : Screen("settings")
}
