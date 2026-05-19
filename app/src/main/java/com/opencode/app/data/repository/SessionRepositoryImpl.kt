package com.opencode.app.data.repository

import com.opencode.app.domain.model.Message
import com.opencode.app.domain.model.MessagePart
import com.opencode.app.domain.model.MessageRole
import com.opencode.app.domain.model.ModelInfo
import com.opencode.app.domain.model.Session
import com.opencode.app.domain.model.SessionStatus
import com.opencode.app.domain.repository.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionRepositoryImpl @Inject constructor() : SessionRepository {

    private val now = Instant.now()

    private val mockSessions = mutableListOf(
        Session(
            id = "session-001",
            title = "创建 Android 项目",
            createdAt = now.minusSeconds(3600 * 2),
            updatedAt = now.minusSeconds(3600),
            status = SessionStatus.IDLE,
            directory = "/home/user/MyApp",
            messages = listOf(
                Message(
                    id = "msg-001-1",
                    sessionId = "session-001",
                    role = MessageRole.USER,
                    parts = listOf(
                        MessagePart.Text("帮我创建一个新的 Android 项目，使用 Kotlin + Jetpack Compose + Hilt")
                    ),
                    createdAt = now.minusSeconds(3600 * 2)
                ),
                Message(
                    id = "msg-001-2",
                    sessionId = "session-001",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("好的，我来帮你创建一个使用 Kotlin + Jetpack Compose + Hilt 的 Android 项目。以下是项目的基本结构和配置："),
                        MessagePart.Code(
                            code = """// build.gradle.kts (Project level)
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.20" apply false
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Code(
                            code = """// build.gradle.kts (App level)
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.example.myapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
}

dependencies {
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Text("项目已创建完成。接下来你需要配置 `AndroidManifest.xml` 和创建 `Application` 类来启用 Hilt。需要我继续吗？")
                    ),
                    createdAt = now.minusSeconds(3600 * 2 + 30),
                    model = ModelInfo(
                        providerId = "openai",
                        modelId = "gpt-4",
                        displayName = "GPT-4"
                    )
                ),
                Message(
                    id = "msg-001-3",
                    sessionId = "session-001",
                    role = MessageRole.USER,
                    parts = listOf(
                        MessagePart.Text("继续，帮我写 Application 类和 MainActivity")
                    ),
                    createdAt = now.minusSeconds(3600)
                ),
                Message(
                    id = "msg-001-4",
                    sessionId = "session-001",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Code(
                            code = """package com.example.myapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application()""",
                            language = "kotlin"
                        ),
                        MessagePart.Code(
                            code = """package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.myapp.ui.theme.MyAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingScreen()
                }
            }
        }
    }
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Text("完成！记得在 `AndroidManifest.xml` 中添加 `android:name=\".MyApplication\"` 到 `<application>` 标签。")
                    ),
                    createdAt = now.minusSeconds(3600 - 30),
                    model = ModelInfo(
                        providerId = "openai",
                        modelId = "gpt-4",
                        displayName = "GPT-4"
                    )
                )
            )
        ),
        Session(
            id = "session-002",
            title = "实现 MVVM 架构",
            createdAt = now.minusSeconds(86400),
            updatedAt = now.minusSeconds(43200),
            status = SessionStatus.IDLE,
            directory = "/home/user/MyApp",
            messages = listOf(
                Message(
                    id = "msg-002-1",
                    sessionId = "session-002",
                    role = MessageRole.USER,
                    parts = listOf(
                        MessagePart.Text("帮我在项目中实现 MVVM 架构，包含 ViewModel 和 Repository 层")
                    ),
                    createdAt = now.minusSeconds(86400)
                ),
                Message(
                    id = "msg-002-2",
                    sessionId = "session-002",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("好的，我来帮你搭建 MVVM 架构。首先创建 Repository 层："),
                        MessagePart.Code(
                            code = """package com.example.myapp.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserProfile(): Flow<UserProfile>
    suspend fun updateUserName(name: String): Result<Unit>
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Code(
                            code = """package com.example.myapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapp.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val userProfile: StateFlow<UserProfile> = userRepository.getUserProfile()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    fun updateName(name: String) {
        viewModelScope.launch {
            userRepository.updateUserName(name)
        }
    }
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Text("MVVM 架构已搭建完成。Repository 负责数据获取，ViewModel 处理 UI 逻辑并通过 StateFlow 暴露状态给 Compose。")
                    ),
                    createdAt = now.minusSeconds(86400 - 60),
                    model = ModelInfo(
                        providerId = "anthropic",
                        modelId = "claude-3-sonnet",
                        displayName = "Claude 3 Sonnet"
                    )
                )
            )
        ),
        Session(
            id = "session-003",
            title = "添加网络请求层",
            createdAt = now.minusSeconds(172800),
            updatedAt = now.minusSeconds(86400),
            status = SessionStatus.RUNNING,
            directory = "/home/user/MyApp",
            messages = listOf(
                Message(
                    id = "msg-003-1",
                    sessionId = "session-003",
                    role = MessageRole.USER,
                    parts = listOf(
                        MessagePart.Text("帮我添加 Retrofit 网络请求层，包含拦截器和错误处理")
                    ),
                    createdAt = now.minusSeconds(172800)
                ),
                Message(
                    id = "msg-003-2",
                    sessionId = "session-003",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("好的，我来帮你配置完整的网络请求层。首先创建 DI 模块："),
                        MessagePart.Code(
                            code = """@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.example.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}""",
                            language = "kotlin"
                        ),
                        MessagePart.Text("网络层配置完成。包含了请求头拦截器、日志拦截器和超时配置。需要我继续添加 API 接口定义和错误处理吗？")
                    ),
                    createdAt = now.minusSeconds(172800 - 45),
                    model = ModelInfo(
                        providerId = "openai",
                        modelId = "gpt-4",
                        displayName = "GPT-4"
                    )
                ),
                Message(
                    id = "msg-003-3",
                    sessionId = "session-003",
                    role = MessageRole.USER,
                    parts = listOf(
                        MessagePart.Text("继续，添加 API 接口和统一的错误处理")
                    ),
                    createdAt = now.minusSeconds(86400)
                )
            )
        )
    )

    override fun getSessions(): Flow<List<Session>> = flow {
        emit(mockSessions.toList())
    }

    override fun getSession(id: String): Flow<Session?> = flow {
        emit(mockSessions.find { it.id == id })
    }

    override fun createSession(title: String): Flow<Session> = flow {
        val newSession = Session(
            id = "session-${UUID.randomUUID()}",
            title = title,
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            status = SessionStatus.IDLE,
            messages = emptyList()
        )
        mockSessions.add(0, newSession)
        emit(newSession)
    }

    override fun deleteSession(id: String): Flow<Boolean> = flow {
        val removed = mockSessions.removeAll { it.id == id }
        emit(removed)
    }

    override fun updateSession(id: String, title: String): Flow<Boolean> = flow {
        val index = mockSessions.indexOfFirst { it.id == id }
        if (index != -1) {
            mockSessions[index] = mockSessions[index].copy(
                title = title,
                updatedAt = Instant.now()
            )
            emit(true)
        } else {
            emit(false)
        }
    }
}
