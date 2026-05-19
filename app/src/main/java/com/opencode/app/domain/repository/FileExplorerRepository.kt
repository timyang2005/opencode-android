package com.opencode.app.domain.repository

import com.opencode.app.domain.model.FileInfo
import kotlinx.coroutines.flow.Flow

interface FileExplorerRepository {
    fun getFiles(path: String): Flow<List<FileInfo>>
    fun getFileContent(path: String): Flow<String?>
    fun createFile(path: String, name: String): Flow<Boolean>
    fun createFolder(path: String, name: String): Flow<Boolean>
}