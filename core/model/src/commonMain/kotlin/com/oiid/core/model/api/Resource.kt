package com.oiid.core.model.api

sealed class Resource<out T> {
    data class Success<out T>(val data: T, val isLoading: Boolean = false) : Resource<T>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
    data object Loading : Resource<Nothing>()
}
