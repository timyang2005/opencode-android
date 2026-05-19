package com.opencode.app.presentation.screens.explorer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.opencode.app.domain.model.FileInfo
import com.opencode.app.domain.repository.FileExplorerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 文件浏览器 ViewModel
 */
@HiltViewModel
class FileExplorerViewModel @Inject constructor(
    private val fileExplorerRepository: FileExplorerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FileExplorerUiState())
    val uiState: StateFlow<FileExplorerUiState> = _uiState.asStateFlow()

    init {
        loadFiles("/")
    }

    private fun loadFiles(path: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null
            )
            try {
                val files = fileExplorerRepository.getFiles(path).first()
                _uiState.value = _uiState.value.copy(
                    currentPath = path,
                    files = files,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "加载文件列表失败"
                )
            }
        }
    }

    fun navigateToDirectory(path: String) {
        loadFiles(path)
    }

    fun navigateUp() {
        val currentPath = _uiState.value.currentPath
        if (currentPath == "/") return
        val parentPath = currentPath.substringBeforeLast("/", "")
        val normalizedParent = if (parentPath.isBlank()) "/" else parentPath
        loadFiles(normalizedParent)
    }

    fun navigateToRoot() {
        loadFiles("/")
    }

    fun openFile(file: FileInfo) {
        viewModelScope.launch {
            try {
                val content = fileExplorerRepository.getFileContent(file.path).first()
                // 文件内容已获取，可由 UI 层展示
            } catch (_: Exception) {
                // 打开文件失败
            }
        }
    }

    fun refresh() {
        loadFiles(_uiState.value.currentPath)
    }

    fun search() {
        // 搜索功能由 UI 层触发搜索对话框
        // ViewModel 仅负责当前文件列表的刷新
    }

    fun createFile(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            try {
                val currentPath = _uiState.value.currentPath
                val fullPath = if (currentPath.endsWith("/")) {
                    "$currentPath$name"
                } else {
                    "$currentPath/$name"
                }
                fileExplorerRepository.createFile(currentPath, name).first()
                // 创建成功后刷新文件列表
                loadFiles(currentPath)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "创建文件失败"
                )
            }
        }
    }

    fun createFolder(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch {
            try {
                val currentPath = _uiState.value.currentPath
                fileExplorerRepository.createFolder(currentPath, name).first()
                // 创建成功后刷新文件列表
                loadFiles(currentPath)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message ?: "创建文件夹失败"
                )
            }
        }
    }
}
