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
                        MessagePart.Text("好的，我来帮你创建项目。首先配置 build.gradle.kts，添加 Compose 和 Hilt 依赖。"),
                        MessagePart.Code(
                            code = "// build.gradle.kts (app level)\nplugins {\n    id(\"com.android.application\")\n    id(\"org.jetbrains.kotlin.android\")\n    id(\"com.google.dagger.hilt.android\")\n    id(\"com.google.devtools.ksp\")\n}\n\nandroid {\n    namespace = \"com.example.myapp\"\n    compileSdk = 34\n    defaultConfig {\n        applicationId = \"com.example.myapp\"\n        minSdk = 24\n        targetSdk = 34\n    }\n    buildFeatures { compose = true }\n}",
                            language = "kotlin"
                        ),
                        MessagePart.Text("接下来创建 Application 类和 MainActivity，记得在 Manifest 中注册。")
                    ),
                    createdAt = now.minusSeconds(3600 * 2 + 30),
                    model = ModelInfo(providerId = "openai", modelId = "gpt-4", displayName = "GPT-4")
                ),
                Message(
                    id = "msg-001-3",
                    sessionId = "session-001",
                    role = MessageRole.USER,
                    parts = listOf(MessagePart.Text("继续，帮我写 MainActivity")),
                    createdAt = now.minusSeconds(3600)
                ),
                Message(
                    id = "msg-001-4",
                    sessionId = "session-001",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("MainActivity 已创建完成，使用 setContent 配置 Compose 主题和 Scaffold。项目基础架构搭建完毕。")
                    ),
                    createdAt = now.minusSeconds(3600 - 30),
                    model = ModelInfo(providerId = "openai", modelId = "gpt-4", displayName = "GPT-4")
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
                    parts = listOf(MessagePart.Text("帮我在项目中实现 MVVM 架构，包含 ViewModel 和 Repository 层")),
                    createdAt = now.minusSeconds(86400)
                ),
                Message(
                    id = "msg-002-2",
                    sessionId = "session-002",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("好的，MVVM 架构包含三层："),
                        MessagePart.Code(
                            code = "// Repository 接口\ninterface UserRepository {\n    fun getUserProfile(): Flow<UserProfile>\n    suspend fun updateUserName(name: String)\n}\n\n// ViewModel\n@HiltViewModel\nclass UserViewModel @Inject constructor(\n    private val repository: UserRepository\n) : ViewModel() {\n    val profile = repository.getUserProfile()\n        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())\n}",
                            language = "kotlin"
                        ),
                        MessagePart.Text("MVVM 架构已搭建完成。Repository 负责数据获取，ViewModel 处理 UI 逻辑并通过 StateFlow 暴露状态给 Compose。")
                    ),
                    createdAt = now.minusSeconds(86400 - 60),
                    model = ModelInfo(providerId = "anthropic", modelId = "claude-3-sonnet", displayName = "Claude 3 Sonnet")
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
                    parts = listOf(MessagePart.Text("帮我添加 Retrofit 网络请求层，包含拦截器和错误处理")),
                    createdAt = now.minusSeconds(172800)
                ),
                Message(
                    id = "msg-003-2",
                    sessionId = "session-003",
                    role = MessageRole.ASSISTANT,
                    parts = listOf(
                        MessagePart.Text("好的，我来帮你配置网络请求层。创建了 NetworkModule 提供 OkHttpClient 和 Retrofit 实例，包含日志拦截器和超时配置。"),
                        MessagePart.Code(
                            code = "// NetworkModule.kt\n@Module\n@InstallIn(SingletonComponent::class)\nobject NetworkModule {\n    @Provides @Singleton\n    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()\n        .addInterceptor(HttpLoggingInterceptor())\n        .connectTimeout(30, TimeUnit.SECONDS)\n        .build()\n\n    @Provides @Singleton\n    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()\n        .baseUrl(\"https://api.example.com/\")\n        .client(client)\n        .addConverterFactory(GsonConverterFactory.create())\n        .build()\n}",
                            language = "kotlin"
                        ),
                        MessagePart.Text("网络层配置完成。需要我继续添加 API 接口定义和错误处理吗？")
                    ),
                    createdAt = now.minusSeconds(172800 - 45),
                    model = ModelInfo(providerId = "openai", modelId = "gpt-4", displayName = "GPT-4")
                ),
                Message(
                    id = "msg-003-3",
                    sessionId = "session-003",
                    role = MessageRole.USER,
                    parts = listOf(MessagePart.Text("继续，添加 API 接口和统一的错误处理")),
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
            mockSessions[index] = mockSessions[index].copy(title = title, updatedAt = Instant.now())
            emit(true)
        } else {
            emit(false)
        }
    }
}
