package com.opencode.app.di

import com.opencode.app.data.repository.ChatRepositoryImpl
import com.opencode.app.data.repository.FileExplorerRepositoryImpl
import com.opencode.app.data.repository.SessionRepositoryImpl
import com.opencode.app.data.repository.SettingsRepositoryImpl
import com.opencode.app.domain.repository.ChatRepository
import com.opencode.app.domain.repository.FileExplorerRepository
import com.opencode.app.domain.repository.SessionRepository
import com.opencode.app.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindChatRepository(impl: ChatRepositoryImpl): ChatRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindFileExplorerRepository(impl: FileExplorerRepositoryImpl): FileExplorerRepository
}