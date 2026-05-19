package com.opencode.app.domain.repository

import com.opencode.app.domain.model.ServerConfig
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getServerConfig(): Flow<ServerConfig>
    fun saveServerConfig(config: ServerConfig): Flow<Boolean>
    fun isDarkMode(): Flow<Boolean>
    fun setDarkMode(enabled: Boolean): Flow<Boolean>
    fun getCodeTheme(): Flow<String>
    fun setCodeTheme(theme: String): Flow<Boolean>
    fun isNotificationsEnabled(): Flow<Boolean>
    fun setNotificationsEnabled(enabled: Boolean): Flow<Boolean>
    fun isSoundEnabled(): Flow<Boolean>
    fun setSoundEnabled(enabled: Boolean): Flow<Boolean>
    suspend fun testConnection(config: ServerConfig): Boolean
}