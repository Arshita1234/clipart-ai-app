package com.example.clipartapp.ui.upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clipartapp.data.repository.ImageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class UploadState(
    val selectedImageUri: Uri? = null,
    val uploadedUrl: String? = null,
    val isUploading: Boolean = false,
    val error: String? = null
)

class UploadViewModel : ViewModel() {
    private val repository = ImageRepository()
    private val _state = MutableStateFlow(UploadState())
    val state = _state.asStateFlow()

    fun onImageSelected(uri: Uri) {
        _state.value = _state.value.copy(selectedImageUri = uri, uploadedUrl = null, error = null)
    }

    fun uploadImage(context: Context, onSuccess: (String) -> Unit) {
        val uri = _state.value.selectedImageUri ?: return
        viewModelScope.launch {
            _state.value = _state.value.copy(isUploading = true, error = null)
            try {
                val url = repository.uploadImageToImgBB(context, uri)
                _state.value = _state.value.copy(isUploading = false, uploadedUrl = url)
                onSuccess(url)
            } catch (e: Exception) {
                _state.value = _state.value.copy(isUploading = false, error = e.message)
            }
        }
    }
}