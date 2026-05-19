package com.opencode.app.domain.repository

import com.opencode.app.domain.model.Session
import kotlinx.coroutines.flow.Flow

interface SessionRepository {
    fun getSessions(): Flow<List<Session>>
    fun getSession(id: String): Flow<Session?>
    fun createSession(title: String): Flow<Session>
    fun deleteSession(id: String): Flow<Boolean>
    fun updateSession(id: String, title: String): Flow<Boolean>
}