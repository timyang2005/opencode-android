package com.opencode.app.presentation.screens.settings

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.app.domain.model.ServerConfig
import com.opencode.app.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置界面 ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                val config = settingsRepository.getServerConfig().first()
                val darkMode = settingsRepository.isDarkMode().first()
                val codeTheme = settingsRepository.getCodeTheme().first()
                val notificationsEnabled = settingsRepository.isNotificationsEnabled().first()
                val soundEnabled = settingsRepository.isSoundEnabled().first()

                _uiState.value = _uiState.value.copy(
                    config = config,
                    darkMode = darkMode,
                    codeTheme = codeTheme,
                    notificationsEnabled = notificationsEnabled,
                    soundEnabled = soundEnabled
                )
            } catch (_: Exception) {
                // 加载失败时使用默认值
            }
        }
    }

    fun updateHost(host: String) {
        _uiState.value = _uiState.value.copy(
            config = _uiState.value.config.copy(host = host)
        )
    }

    fun updatePort(port: Int) {
        _uiState.value = _uiState.value.copy(
            config = _uiState.value.config.copy(port = port)
        )
    }

    fun updateUseHttps(useHttps: Boolean) {
        _uiState.value = _uiState.value.copy(
            config = _uiState.value.config.copy(useHttps = useHttps)
        )
    }

    fun updateUsername(username: String) {
        _uiState.value = _uiState.value.copy(
            config = _uiState.value.config.copy(username = username)
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            config = _uiState.value.config.copy(password = password.ifBlank { null })
        )
    }

    fun testConnection() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isTestingConnection = true,
                connectionResult = null
            )
            try {
                val success = settingsRepository.testConnection(_uiState.value.config)
                _uiState.value = _uiState.value.copy(
                    isTestingConnection = false,
                    connectionResult = ConnectionResult(
                        success = success,
                        message = if (success) "连接成功" else "连接失败，请检查服务器地址和端口"
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isTestingConnection = false,
                    connectionResult = ConnectionResult(
                        success = false,
                        message = "连接失败: ${e.message ?: "未知错误"}"
                    )
                )
            }
        }
    }

    fun updateDarkMode(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(darkMode = enabled)
    }

    fun updateCodeTheme(theme: String) {
        _uiState.value = _uiState.value.copy(codeTheme = theme)
    }

    fun updateNotificationsEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(notificationsEnabled = enabled)
    }

    fun updateSoundEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(soundEnabled = enabled)
    }

    fun saveSettings() {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                settingsRepository.saveServerConfig(state.config).first()
                settingsRepository.setDarkMode(state.darkMode).first()
                settingsRepository.setCodeTheme(state.codeTheme).first()
                settingsRepository.setNotificationsEnabled(state.notificationsEnabled).first()
                settingsRepository.setSoundEnabled(state.soundEnabled).first()
            } catch (_: Exception) {
                // 保存失败
            }
        }
    }

    fun openGitHub() {
        viewModelScope.launch {
            // 由 UI 层处理 Intent 跳转
            // 此方法仅作为 ViewModel 层的意图声明
        }
    }
}
