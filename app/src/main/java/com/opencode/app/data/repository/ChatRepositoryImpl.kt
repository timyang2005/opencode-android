package com.opencode.app.data.repository

import com.opencode.app.domain.model.Message
import com.opencode.app.domain.model.MessagePart
import com.opencode.app.domain.model.MessageRole
import com.opencode.app.domain.model.ModelInfo
import com.opencode.app.domain.repository.ChatRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val mockMessages = mutableMapOf<String, MutableList<Message>>()

    override fun getMessages(sessionId: String): Flow<List<Message>> = flow {
        emit(mockMessages[sessionId]?.toList() ?: emptyList())
    }

    override fun sendMessage(sessionId: String, text: String): Flow<Message> = flow {
        val userMessage = Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = sessionId,
            role = MessageRole.USER,
            parts = listOf(MessagePart.Text(text)),
            createdAt = Instant.now()
        )
        mockMessages.getOrPut(sessionId) { mutableListOf() }.add(userMessage)

        delay(1000)

        val assistantMessage = generateMockReply(sessionId, text)
        mockMessages[sessionId]?.add(assistantMessage)
        emit(assistantMessage)
    }

    private fun generateMockReply(sessionId: String, userText: String): Message {
        val replyParts = mutableListOf<MessagePart>()

        when {
            userText.contains("代码", ignoreCase = true) || userText.contains("code", ignoreCase = true) -> {
                replyParts.add(MessagePart.Text("好的，这是你需要的代码示例："))
                replyParts.add(MessagePart.Code(
                    code = "fun main() {\n    println(\"Hello, OpenCode!\")\n    val items = listOf(\"Kotlin\", \"Compose\", \"Hilt\")\n    items.forEach { item -> println(\"- \$item\") }\n}",
                    language = "kotlin"
                ))
                replyParts.add(MessagePart.Text("这段代码展示了 Kotlin 的基本语法，包括字符串模板和集合操作。"))
            }
            userText.contains("解释", ignoreCase = true) || userText.contains("explain", ignoreCase = true) -> {
                replyParts.add(MessagePart.Text(
                    "这是一个很好的问题。让我来详细解释：\n\n" +
                    "1. **第一点**：核心概念是模块化和关注点分离。\n" +
                    "2. **第二点**：MVVM 架构将 UI 逻辑与业务逻辑分离。\n" +
                    "3. **第三点**：Hilt 提供了依赖注入的能力。\n\n" +
                    "如果你需要更深入的了解某个方面，请告诉我。"
                ))
            }
            userText.contains("错误", ignoreCase = true) || userText.contains("error", ignoreCase = true) -> {
                replyParts.add(MessagePart.Text(
                    "让我帮你分析一下这个错误。可能的原因有：\n\n" +
                    "- 空指针异常：检查变量是否在使用前已正确初始化\n" +
                    "- 类型不匹配：确认泛型类型是否一致\n" +
                    "- 生命周期问题：确保 ViewModel 中的操作不会在 Activity 销毁后继续执行"
                ))
                replyParts.add(MessagePart.Code(
                    code = "try {\n    // your code here\n} catch (e: Exception) {\n    Log.e(\"TAG\", \"Error: \${e.message}\", e)\n}",
                    language = "kotlin"
                ))
            }
            else -> {
                val replies = listOf(
                    "我理解你的需求。这是一个很好的方向，让我来帮你实现。",
                    "收到！让我分析一下你的问题并提供解决方案。",
                    "好的，我来帮你处理这个任务。首先让我了解一下当前的项目状态。"
                )
                replyParts.add(MessagePart.Text(replies.random()))
            }
        }

        return Message(
            id = "msg-${UUID.randomUUID()}",
            sessionId = sessionId,
            role = MessageRole.ASSISTANT,
            parts = replyParts,
            createdAt = Instant.now(),
            model = ModelInfo(providerId = "openai", modelId = "gpt-4", displayName = "GPT-4")
        )
    }
}
