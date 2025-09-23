package com.geskot.app.data.model

/**
 * Sealed class representing different UI states for async operations
 */
sealed class UiState<out T> {
    /**
     * Initial state before any operation
     */
    object Idle : UiState<Nothing>()

    /**
     * Loading state during async operations
     */
    object Loading : UiState<Nothing>()

    /**
     * Success state with data
     * @param data The successful result data
     */
    data class Success<T>(val data: T) : UiState<T>()

    /**
     * Error state with error message
     * @param message Error description for user display
     * @param exception Optional exception for debugging
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : UiState<Nothing>()
}

/**
 * Extension function to check if state is loading
 */
fun <T> UiState<T>.isLoading(): Boolean = this is UiState.Loading

/**
 * Extension function to check if state is success
 */
fun <T> UiState<T>.isSuccess(): Boolean = this is UiState.Success

/**
 * Extension function to check if state is error
 */
fun <T> UiState<T>.isError(): Boolean = this is UiState.Error

/**
 * Extension function to get data if success, null otherwise
 */
fun <T> UiState<T>.getDataOrNull(): T? = when (this) {
    is UiState.Success -> data
    else -> null
}