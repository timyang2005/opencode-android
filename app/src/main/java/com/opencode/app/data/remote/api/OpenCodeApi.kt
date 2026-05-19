package com.opencode.app.data.remote.api

import com.opencode.app.domain.model.*
import retrofit2.Response
import retrofit2.http.*

interface OpenCodeApi {
    @GET("/global/health")
    suspend fun getHealth(): Response<ServerHealth>

    @GET("/session")
    suspend fun getSessions(): Response<List<SessionResponse>>

    @POST("/session")
    suspend fun createSession(
        @Body request: CreateSessionRequest,
        @Query("directory") directory: String? = null
    ): Response<SessionResponse>

    @GET("/session/{id}")
    suspend fun getSession(@Path("id") sessionId: String): Response<SessionDetailResponse>

    @DELETE("/session/{id}")
    suspend fun deleteSession(@Path("id") sessionId: String): Response<Boolean>

    @POST("/session/{id}/message")
    suspend fun sendMessage(
        @Path("id") sessionId: String,
        @Body request: SendMessageApiRequest
    ): Response<MessageResponse>

    @GET("/file")
    suspend fun getFile(@Query("path") path: String): Response<FileContentResponse>

    @GET("/project")
    suspend fun getProjects(): Response<List<ProjectInfo>>
}

data class SessionResponse(
    val id: String,
    val title: String,
    val parentID: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val status: String,
    val isShared: Boolean = false
)

data class SessionDetailResponse(
    val info: SessionResponse,
    val messages: List<MessageResponse>
)

data class MessageResponse(
    val id: String,
    val role: String,
    val parts: List<MessagePartResponse>,
    val createdAt: String
)

data class MessagePartResponse(
    val type: String,
    val text: String? = null,
    val code: String? = null
)

data class SendMessageApiRequest(
    val model: ModelInfoRequest,
    val parts: List<MessagePartRequest>
)

data class ModelInfoRequest(val providerID: String, val modelID: String)

data class MessagePartRequest(val type: String = "text", val text: String)

data class CreateSessionRequest(
    val title: String? = null,
    val parentId: String? = null
)

data class FileContentResponse(
    val path: String,
    val content: String
)

data class ProjectInfo(
    val id: String,
    val name: String,
    val path: String
)