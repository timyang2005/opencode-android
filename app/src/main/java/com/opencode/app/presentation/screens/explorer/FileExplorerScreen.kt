package com.opencode.app.presentation.screens.explorer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CreateNewFolder
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NoteAdd
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.opencode.app.domain.model.FileInfo

data class FileExplorerUiState(
    val currentPath: String = "/",
    val files: List<FileInfo> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedFile: FileInfo? = null,
    val showCreateDialog: Boolean = false,
    val createDialogType: String = "file"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileExplorerScreen(
    viewModel: FileExplorerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFabMenu by remember { mutableStateOf(false) }
    var showCreateDialog by remember { mutableStateOf(false) }
    var createDialogType by remember { mutableStateOf("file") }
    var createItemName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "File Explorer",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        PathBreadcrumb(
                            currentPath = uiState.currentPath,
                            onPathSegmentClick = { path ->
                                viewModel.navigateToDirectory(path)
                            }
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                    if (uiState.currentPath != "/") {
                        IconButton(onClick = { viewModel.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.NavigateBefore,
                                contentDescription = "Go up"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                DropdownMenu(
                    expanded = showFabMenu,
                    onDismissRequest = { showFabMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("New File") },
                        leadingIcon = {
                            Icon(Icons.Default.NoteAdd, contentDescription = null)
                        },
                        onClick = {
                            showFabMenu = false
                            createItemName = ""
                            createDialogType = "file"
                            showCreateDialog = true
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("New Folder") },
                        leadingIcon = {
                            Icon(Icons.Default.CreateNewFolder, contentDescription = null)
                        },
                        onClick = {
                            showFabMenu = false
                            createItemName = ""
                            createDialogType = "folder"
                            showCreateDialog = true
                        }
                    )
                }

                FloatingActionButton(
                    onClick = { showFabMenu = !showFabMenu }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create new"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = uiState.error ?: "An error occurred",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        TextButton(onClick = { viewModel.refresh() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.files.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FolderOpen,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Empty directory",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This folder is empty. Tap + to create a file or folder.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        val directories = uiState.files.filter { it.isDirectory }
                        val regularFiles = uiState.files.filter { !it.isDirectory }

                        if (directories.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Folders",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                            items(
                                items = directories,
                                key = { it.path }
                            ) { file ->
                                FileItem(
                                    fileInfo = file,
                                    onClick = { viewModel.navigateToDirectory(file.path) }
                                )
                            }
                        }

                        if (regularFiles.isNotEmpty()) {
                            item {
                                Text(
                                    text = "Files",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                                )
                            }
                            items(
                                items = regularFiles,
                                key = { it.path }
                            ) { file ->
                                FileItem(
                                    fileInfo = file,
                                    onClick = { viewModel.openFile(file) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        AlertDialog(
            onDismissRequest = {
                showCreateDialog = false
                createItemName = ""
            },
            title = {
                Text(
                    if (createDialogType == "folder") "New Folder" else "New File"
                )
            },
            text = {
                OutlinedTextField(
                    value = createItemName,
                    onValueChange = { createItemName = it },
                    label = {
                        Text(
                            if (createDialogType == "folder") "Folder name" else "File name"
                        )
                    },
                    placeholder = {
                        Text(
                            if (createDialogType == "folder") "my_folder" else "myfile.kt"
                        )
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (createItemName.isNotBlank()) {
                            if (createDialogType == "folder") {
                                viewModel.createFolder(createItemName.trim())
                            } else {
                                viewModel.createFile(createItemName.trim())
                            }
                            createItemName = ""
                            showCreateDialog = false
                        }
                    },
                    enabled = createItemName.isNotBlank()
                ) {
                    Text("Create")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    createItemName = ""
                    showCreateDialog = false
                }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun PathBreadcrumb(
    currentPath: String,
    onPathSegmentClick: (String) -> Unit
) {
    val segments = if (currentPath == "/") {
        listOf("/" to "/")
    } else {
        listOf("/" to "/") + currentPath
            .trim('/')
            .split("/")
            .fold(mutableListOf<Pair<String, String>>()) { acc, segment ->
                val parentPath = if (acc.isEmpty()) "/" else acc.last().second
                val fullPath = if (parentPath == "/") "/$segment" else "$parentPath/$segment"
                acc.add(segment to fullPath)
                acc
            }
    }

    Row(
        modifier = Modifier.padding(top = 4.dp)
    ) {
        segments.forEachIndexed { index, (segment, path) ->
            if (index > 0) {
                Text(
                    text = " / ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = segment,
                style = MaterialTheme.typography.labelSmall,
                color = if (index == segments.lastIndex) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.clickable { onPathSegmentClick(path) }
            )
        }
    }
}

@Composable
private fun FileItem(
    fileInfo: FileInfo,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (fileInfo.isDirectory) {
                    Icons.Default.Folder
                } else {
                    getFileIcon(fileInfo.name)
                },
                contentDescription = null,
                tint = if (fileInfo.isDirectory) {
                    Color(0xFFFFC107)
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fileInfo.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!fileInfo.isDirectory && fileInfo.size > 0) {
                        Text(
                            text = formatFileSize(fileInfo.size),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    fileInfo.modifiedAt?.let { timestamp ->
                        Text(
                            text = formatTimestamp(timestamp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

private fun getFileIcon(name: String): ImageVector {
    val extension = name.substringAfterLast('.', "").lowercase()
    return when (extension) {
        "kt", "java", "py", "js", "ts", "cpp", "c", "h", "rs", "go" -> Icons.Default.Description
        "md", "txt", "json", "xml", "yaml", "yml", "toml" -> Icons.Default.Description
        "png", "jpg", "jpeg", "gif", "svg", "webp" -> Icons.Default.Image
        else -> Icons.Default.InsertDriveFile
    }
}

private fun formatFileSize(bytes: Long): String {
    return when {
        bytes < 1024 -> "$bytes B"
        bytes < 1024 * 1024 -> "${bytes / 1024} KB"
        bytes < 1024 * 1024 * 1024 -> "${bytes / (1024 * 1024)} MB"
        else -> "${bytes / (1024 * 1024 * 1024)} GB"
    }
}

private fun formatTimestamp(timestamp: java.time.Instant): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
    return sdf.format(java.util.Date.from(timestamp))
}
