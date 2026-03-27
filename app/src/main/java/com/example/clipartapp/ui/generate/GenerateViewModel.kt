package com.example.clipartapp.ui.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clipartapp.data.model.ClipartStyle
import com.example.clipartapp.data.model.GeneratedClipart
import com.example.clipartapp.data.repository.ImageRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenerateViewModel : ViewModel() {

    private val repository = ImageRepository()

    private val _results = MutableStateFlow<List<GeneratedClipart>>(emptyList())
    val results = _results.asStateFlow()

    fun generateAll(imageUrl: String, styles: List<ClipartStyle>) {
        _results.value = styles.map {
            GeneratedClipart(it, null, isLoading = true)
        }

        viewModelScope.launch {
            val jobs = styles.mapIndexed { index, style ->
                async {
                    try {
                        val result = repository.generateClipart(imageUrl, style)

                        val updated = _results.value.toMutableList()
                        updated[index] = GeneratedClipart(style, result, false)
                        _results.value = updated

                    } catch (e: Exception) {
                        val updated = _results.value.toMutableList()
                        updated[index] = GeneratedClipart(style, null, false, e.message)
                        _results.value = updated
                    }
                }
            }
            jobs.forEach { it.await() }
        }
    }
}