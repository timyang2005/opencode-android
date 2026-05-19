package com.opencode.app.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.opencode.app.domain.model.ServerConfig
import com.opencode.app.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object Keys {
        val SERVER_HOST = stringPreferencesKey("server_host")
        val SERVER_PORT = intPreferencesKey("server_port")
        val SERVER_USE_HTTPS = booleanPreferencesKey("server_use_https")
        val SERVER_USERNAME = stringPreferencesKey("server_username")
        val SERVER_PASSWORD = stringPreferencesKey("server_password")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val CODE_THEME = stringPreferencesKey("code_theme")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    }

    override fun getServerConfig(): Flow<ServerConfig> {
        return dataStore.data.map { prefs ->
            ServerConfig(
                host = prefs[Keys.SERVER_HOST] ?: "localhost",
                port = prefs[Keys.SERVER_PORT] ?: 4096,
                useHttps = prefs[Keys.SERVER_USE_HTTPS] ?: false,
                username = prefs[Keys.SERVER_USERNAME] ?: "opencode",
                password = prefs[Keys.SERVER_PASSWORD]
            )
        }
    }

    override fun saveServerConfig(config: ServerConfig): Flow<Boolean> = flow {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.SERVER_HOST] = config.host
                prefs[Keys.SERVER_PORT] = config.port
                prefs[Keys.SERVER_USE_HTTPS] = config.useHttps
                prefs[Keys.SERVER_USERNAME] = config.username
                config.password?.let { prefs[Keys.SERVER_PASSWORD] = it }
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun isDarkMode(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[Keys.DARK_MODE] ?: false
        }
    }

    override fun setDarkMode(enabled: Boolean): Flow<Boolean> = flow {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.DARK_MODE] = enabled
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun getCodeTheme(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[Keys.CODE_THEME] ?: "Default"
        }
    }

    override fun setCodeTheme(theme: String): Flow<Boolean> = flow {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.CODE_THEME] = theme
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun isNotificationsEnabled(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] ?: true
        }
    }

    override fun setNotificationsEnabled(enabled: Boolean): Flow<Boolean> = flow {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.NOTIFICATIONS_ENABLED] = enabled
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun isSoundEnabled(): Flow<Boolean> {
        return dataStore.data.map { prefs ->
            prefs[Keys.SOUND_ENABLED] ?: true
        }
    }

    override fun setSoundEnabled(enabled: Boolean): Flow<Boolean> = flow {
        try {
            dataStore.edit { prefs ->
                prefs[Keys.SOUND_ENABLED] = enabled
            }
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override suspend fun testConnection(config: ServerConfig): Boolean {
        // 模拟连接测试：仅检查配置是否有效
        return config.host.isNotBlank() && config.port in 1..65535
    }
}
