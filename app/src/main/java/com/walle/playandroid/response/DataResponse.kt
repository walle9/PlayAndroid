package com.walle.playandroid.response

const val RESULT_OK = 0
fun <T> covertData(netResponse:NetResponse<T>): DataResponse<T> {
    val code = netResponse.errorCode
    val message = netResponse.errorMsg
    return when (code) {
        RESULT_OK -> {
            val data = netResponse.data
            if (data != null) {
                DataResponse.Success(data)
            } else {
                DataResponse.Error(ErrorType.EMPTY, message, code)
            }
        }
        else -> DataResponse.Error(ErrorType.NET_ERROR, message, code)

    }
}

sealed class DataResponse<out T> {
    data class Success<out T>(val data: T) : DataResponse<T>()
    data class Error(
        val errorType: ErrorType,
        val errorMsg: String? = null,
        val errorCode: Int = 0,
        val error: Throwable? = null
    ) : DataResponse<Nothing>()
}

enum class ErrorType {
    NET_ERROR, EMPTY,NET_ERROR_CONNECT,ERROR
}