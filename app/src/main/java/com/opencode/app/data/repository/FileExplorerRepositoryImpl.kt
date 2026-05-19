package com.opencode.app.data.repository

import com.opencode.app.domain.model.FileInfo
import com.opencode.app.domain.repository.FileExplorerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileExplorerRepositoryImpl @Inject constructor() : FileExplorerRepository {

    private val now = Instant.now()

    private val fileTree = mutableMapOf(
        "" to listOf(
            FileInfo("app/", "app", true, 4096, now),
            FileInfo("gradle/", "gradle", true, 4096, now),
            FileInfo("build.gradle.kts", "build.gradle.kts", false, 1024, now),
            FileInfo("settings.gradle.kts", "settings.gradle.kts", false, 256, now),
            FileInfo("gradle.properties", "gradle.properties", false, 128, now),
            FileInfo(".gitignore", ".gitignore", false, 256, now),
            FileInfo("README.md", "README.md", false, 512, now)
        ),
        "app/" to listOf(
            FileInfo("app/src/", "src", true, 4096, now),
            FileInfo("app/build.gradle.kts", "build.gradle.kts", false, 2048, now),
            FileInfo("app/proguard-rules.pro", "proguard-rules.pro", false, 512, now)
        ),
        "app/src/" to listOf(
            FileInfo("app/src/main/", "main", true, 4096, now),
            FileInfo("app/src/test/", "test", true, 4096, now),
            FileInfo("app/src/androidTest/", "androidTest", true, 4096, now)
        ),
        "app/src/main/" to listOf(
            FileInfo("app/src/main/java/", "java", true, 4096, now),
            FileInfo("app/src/main/res/", "res", true, 4096, now),
            FileInfo("app/src/main/AndroidManifest.xml", "AndroidManifest.xml", false, 1024, now)
        ),
        "app/src/main/java/" to listOf(
            FileInfo("app/src/main/java/com/", "com", true, 4096, now)
        ),
        "app/src/main/java/com/" to listOf(
            FileInfo("app/src/main/java/com/opencode/", "opencode", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/", "app", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/MainActivity.kt", "MainActivity.kt", false, 2048, now),
            FileInfo("app/src/main/java/com/opencode/app/OpenCodeApplication.kt", "OpenCodeApplication.kt", false, 256, now),
            FileInfo("app/src/main/java/com/opencode/app/di/", "di", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/domain/", "domain", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/data/", "data", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/", "ui", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/di/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/di/NetworkModule.kt", "NetworkModule.kt", false, 1536, now),
            FileInfo("app/src/main/java/com/opencode/app/di/RepositoryModule.kt", "RepositoryModule.kt", false, 1024, now),
            FileInfo("app/src/main/java/com/opencode/app/di/DatabaseModule.kt", "DatabaseModule.kt", false, 512, now)
        ),
        "app/src/main/java/com/opencode/app/domain/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/domain/model/", "model", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/domain/repository/", "repository", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/domain/model/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/domain/model/Session.kt", "Session.kt", false, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/domain/repository/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/domain/repository/SessionRepository.kt", "SessionRepository.kt", false, 768, now),
            FileInfo("app/src/main/java/com/opencode/app/domain/repository/ChatRepository.kt", "ChatRepository.kt", false, 512, now),
            FileInfo("app/src/main/java/com/opencode/app/domain/repository/SettingsRepository.kt", "SettingsRepository.kt", false, 768, now),
            FileInfo("app/src/main/java/com/opencode/app/domain/repository/FileExplorerRepository.kt", "FileExplorerRepository.kt", false, 512, now)
        ),
        "app/src/main/java/com/opencode/app/data/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/data/remote/", "remote", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/data/repository/", "repository", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/data/remote/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/data/remote/api/", "api", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/data/remote/api/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/data/remote/api/OpenCodeApi.kt", "OpenCodeApi.kt", false, 8192, now)
        ),
        "app/src/main/java/com/opencode/app/data/repository/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/data/repository/SessionRepositoryImpl.kt", "SessionRepositoryImpl.kt", false, 6144, now),
            FileInfo("app/src/main/java/com/opencode/app/data/repository/ChatRepositoryImpl.kt", "ChatRepositoryImpl.kt", false, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/data/repository/SettingsRepositoryImpl.kt", "SettingsRepositoryImpl.kt", false, 3072, now),
            FileInfo("app/src/main/java/com/opencode/app/data/repository/FileExplorerRepositoryImpl.kt", "FileExplorerRepositoryImpl.kt", false, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/ui/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/ui/theme/", "theme", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/", "screens", true, 4096, now)
        ),
        "app/src/main/java/com/opencode/app/ui/theme/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/ui/theme/Color.kt", "Color.kt", false, 1024, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/theme/Theme.kt", "Theme.kt", false, 2048, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/theme/Type.kt", "Type.kt", false, 1024, now)
        ),
        "app/src/main/java/com/opencode/app/ui/screens/" to listOf(
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/main/", "main", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/sessions/", "sessions", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/chat/", "chat", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/settings/", "settings", true, 4096, now),
            FileInfo("app/src/main/java/com/opencode/app/ui/screens/explorer/", "explorer", true, 4096, now)
        ),
        "app/src/main/res/" to listOf(
            FileInfo("app/src/main/res/drawable/", "drawable", true, 4096, now),
            FileInfo("app/src/main/res/mipmap-anydpi-v26/", "mipmap-anydpi-v26", true, 4096, now),
            FileInfo("app/src/main/res/values/", "values", true, 4096, now)
        ),
        "app/src/main/res/values/" to listOf(
            FileInfo("app/src/main/res/values/colors.xml", "colors.xml", false, 512, now),
            FileInfo("app/src/main/res/values/strings.xml", "strings.xml", false, 256, now),
            FileInfo("app/src/main/res/values/themes.xml", "themes.xml", false, 1024, now)
        ),
        "gradle/" to listOf(
            FileInfo("gradle/wrapper/", "wrapper", true, 4096, now)
        ),
        "gradle/wrapper/" to listOf(
            FileInfo("gradle/wrapper/gradle-wrapper.jar", "gradle-wrapper.jar", false, 65536, now),
            FileInfo("gradle/wrapper/gradle-wrapper.properties", "gradle-wrapper.properties", false, 128, now)
        )
    )

    private val fileContents = mapOf(
        "build.gradle.kts" to "// Top-level build file\nplugins {\n    id(\"com.android.application\") version \"8.2.0\" apply false\n    id(\"org.jetbrains.kotlin.android\") version \"1.9.20\" apply false\n}",
        "settings.gradle.kts" to "pluginManagement {\n    repositories { google(); mavenCentral() }\n}\nrootProject.name = \"OpenCode\"\ninclude(\":app\")",
        "gradle.properties" to "org.gradle.jvmargs=-Xmx2048m\nandroid.useAndroidX=true\nkotlin.code.style=official"
    )

    override fun getFiles(path: String): Flow<List<FileInfo>> = flow {
        val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
        emit(fileTree[normalizedPath] ?: fileTree[path.removeSuffix("/")] ?: emptyList())
    }

    override fun getFileContent(path: String): Flow<String?> = flow {
        emit(fileContents[path] ?: fileContents[path.removePrefix("./")] ?: fileContents[path.removePrefix("/")])
    }

    override fun createFile(path: String, name: String): Flow<Boolean> = flow {
        val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
        val existingFiles = fileTree[normalizedPath]?.toMutableList() ?: mutableListOf()
        val fullPath = "$normalizedPath$name"
        existingFiles.add(FileInfo(fullPath, name, false, 0, Instant.now()))
        fileTree[normalizedPath] = existingFiles
        emit(true)
    }

    override fun createFolder(path: String, name: String): Flow<Boolean> = flow {
        val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
        val existingFiles = fileTree[normalizedPath]?.toMutableList() ?: mutableListOf()
        val fullPath = "$normalizedPath$name/"
        existingFiles.add(FileInfo(fullPath, name, true, 4096, Instant.now()))
        fileTree[normalizedPath] = existingFiles
        fileTree[fullPath] = emptyList()
        emit(true)
    }
}
