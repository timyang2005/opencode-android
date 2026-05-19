package com.opencode.app.presentation.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Http
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opencode.app.domain.model.ServerConfig

data class ConnectionResult(
    val success: Boolean,
    val message: String
)

data class SettingsUiState(
    val config: ServerConfig = ServerConfig(),
    val darkMode: Boolean = false,
    val codeTheme: String = "default",
    val notificationsEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val isTestingConnection: Boolean = false,
    val connectionResult: ConnectionResult? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showPassword by remember { mutableStateOf(false) }
    var showCodeThemeDialog by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Section 1: Server Configuration
            SettingsSection(title = "Server Configuration", icon = Icons.Default.Http) {
                OutlinedTextField(
                    value = uiState.config.host,
                    onValueChange = { viewModel.updateHost(it) },
                    label = { Text("Host") },
                    placeholder = { Text("localhost") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.config.port.toString(),
                    onValueChange = { port ->
                        port.toIntOrNull()?.let { viewModel.updatePort(it) }
                    },
                    label = { Text("Port") },
                    placeholder = { Text("4096") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingToggleRow(
                    title = "Use HTTPS",
                    subtitle = "Enable secure connection",
                    checked = uiState.config.useHttps,
                    onCheckedChange = { viewModel.updateUseHttps(it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { viewModel.testConnection() },
                        enabled = uiState.config.host.isNotBlank() && !uiState.isTestingConnection
                    ) {
                        if (uiState.isTestingConnection) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .height(16.dp)
                                    .width(16.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Testing...")
                        } else {
                            Icon(
                                imageVector = Icons.Default.Http,
                                contentDescription = null,
                                modifier = Modifier.height(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Test Connection")
                        }
                    }
                }

                uiState.connectionResult?.let { result ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (result.success) {
                                MaterialTheme.colorScheme.primaryContainer
                            } else {
                                MaterialTheme.colorScheme.errorContainer
                            }
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (result.success) {
                                    Icons.Default.CheckCircle
                                } else {
                                    Icons.Default.Error
                                },
                                contentDescription = null,
                                tint = if (result.success) {
                                    Color(0xFF4CAF50)
                                } else {
                                    MaterialTheme.colorScheme.error
                                }
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = result.message,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }

            // Section 2: Authentication
            SettingsSection(title = "Authentication", icon = Icons.Default.Key) {
                OutlinedTextField(
                    value = uiState.config.username,
                    onValueChange = { viewModel.updateUsername(it) },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = uiState.config.password ?: "",
                    onValueChange = { viewModel.updatePassword(it) },
                    label = { Text("Password") },
                    placeholder = { Text("Optional") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = if (showPassword) {
                        androidx.compose.ui.text.input.VisualTransformation.None
                    } else {
                        androidx.compose.ui.text.input.PasswordVisualTransformation()
                    },
                    trailingIcon = {
                        IconButton(onClick = { showPassword = !showPassword }) {
                            Icon(
                                imageVector = if (showPassword) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (showPassword) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                }
                            )
                        }
                    }
                )
            }

            // Section 3: Appearance
            SettingsSection(title = "Appearance", icon = Icons.Default.Palette) {
                SettingToggleRow(
                    title = "Dark Mode",
                    subtitle = "Use dark theme throughout the app",
                    checked = uiState.darkMode,
                    onCheckedChange = { viewModel.updateDarkMode(it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Code Theme",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "Syntax highlighting theme for code blocks",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    TextButton(onClick = { showCodeThemeDialog = true }) {
                        Text(uiState.codeTheme)
                    }
                }
            }

            // Section 4: Notifications
            SettingsSection(title = "Notifications", icon = Icons.Default.Notifications) {
                SettingToggleRow(
                    title = "Enable Notifications",
                    subtitle = "Receive push notifications for new messages",
                    checked = uiState.notificationsEnabled,
                    onCheckedChange = { viewModel.updateNotificationsEnabled(it) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                SettingToggleRow(
                    title = "Sound",
                    subtitle = "Play sound for incoming messages",
                    checked = uiState.soundEnabled,
                    onCheckedChange = { viewModel.updateSoundEnabled(it) }
                )
            }

            // Section 5: About
            SettingsSection(title = "About", icon = Icons.Default.Info) {
                SettingInfoRow(
                    label = "App Name",
                    value = "OpenCode"
                )
                SettingInfoRow(
                    label = "Version",
                    value = "1.0.0"
                )
                SettingInfoRow(
                    label = "Build",
                    value = "2025.05.19"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showCodeThemeDialog) {
        val themes = listOf(
            "default", "monokai", "dracula", "github",
            "solarized-light", "solarized-dark", "nord", "one-dark"
        )
        AlertDialog(
            onDismissRequest = { showCodeThemeDialog = false },
            title = { Text("Select Code Theme") },
            text = {
                Column {
                    themes.forEach { theme ->
                        TextButton(
                            onClick = {
                                viewModel.updateCodeTheme(theme)
                                showCodeThemeDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = theme.replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodyLarge
                                )
                                if (theme == uiState.codeTheme) {
                                    Text(
                                        text = "\u2713",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCodeThemeDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.outlineVariant
            )

            content()
        }
    }
}

@Composable
private fun SettingToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun SettingInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace
        )
    }
}
