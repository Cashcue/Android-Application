package com.cashcue.data

sealed class Result<out T> {

    data object Loading: Result<Nothing>()
    class Error(val message: String): Result<Nothing>()
    class Success<T>(val data: T ): Result<T>()

}