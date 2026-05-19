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

    /**
     * Mock 虚拟文件树，模拟典型 Android 项目结构
     */
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
            FileInfo("app/src/main/res/values/", "values", true, 4096, now),
            FileInfo("app/src/main/res/layout/", "layout", true, 4096, now)
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
        ),
        "app/src/test/" to listOf(
            FileInfo("app/src/test/java/", "java", true, 4096, now)
        ),
        "app/src/androidTest/" to listOf(
            FileInfo("app/src/androidTest/java/", "java", true, 4096, now)
        )
    )

    /**
     * Mock 文件内容
     */
    private val fileContents = mapOf(
        "build.gradle.kts" to """// Top-level build file
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("org.jetbrains.kotlin.kapt") version "1.9.20" apply false
}""",
        "settings.gradle.kts" to """pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "OpenCodeAndroidApp"
include(":app")""",
        "gradle.properties" to """org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
android.useAndroidX=true
kotlin.code.style=official
android.nonTransitiveRClass=true""",
        "app/build.gradle.kts" to """plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.opencode.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.opencode.app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-compiler:2.48")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
}""",
        "app/src/main/AndroidManifest.xml" to """<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:name=".OpenCodeApplication"
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.OpenCode">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>"""
    )

    override fun getFiles(path: String): Flow<List<FileInfo>> = flow {
        // 标准化路径：确保以 / 结尾（目录）
        val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
        val files = fileTree[path]
        if (files != null) {
            emit(files)
        } else {
            // 尝试匹配不带尾部斜杠的路径
            emit(fileTree[path.removeSuffix("/")] ?: emptyList())
        }
    }

    override fun getFileContent(path: String): Flow<String?> = flow {
        // 尝试从多个可能的 key 中查找
        val content = fileContents[path]
            ?: fileContents[path.removePrefix("./")]
            ?: fileContents[path.removePrefix("/")]
        emit(content)
    }

    override fun createFile(path: String, name: String): Flow<Boolean> = flow {
        try {
            val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
            val existingFiles = fileTree[normalizedPath]?.toMutableList() ?: mutableListOf()
            val fullPath = if (normalizedPath.endsWith("/")) "$normalizedPath$name" else "$normalizedPath/$name"
            existingFiles.add(FileInfo("$fullPath", name, false, 0, Instant.now()))
            fileTree[normalizedPath] = existingFiles
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }

    override fun createFolder(path: String, name: String): Flow<Boolean> = flow {
        try {
            val normalizedPath = if (path.isNotEmpty() && !path.endsWith("/")) "$path/" else path
            val existingFiles = fileTree[normalizedPath]?.toMutableList() ?: mutableListOf()
            val fullPath = if (normalizedPath.endsWith("/")) "$normalizedPath$name" else "$normalizedPath/$name"
            existingFiles.add(FileInfo("$fullPath/", name, true, 4096, Instant.now()))
            fileTree[normalizedPath] = existingFiles
            fileTree[fullPath] = emptyList()
            emit(true)
        } catch (e: Exception) {
            emit(false)
        }
    }
}
