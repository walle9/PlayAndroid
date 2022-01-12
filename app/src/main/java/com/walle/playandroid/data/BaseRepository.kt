package com.walle.playandroid.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.walle.playandroid.response.DataResponse
import com.walle.playandroid.response.ErrorType
import com.walle.playandroid.response.NetResponse
import com.walle.playandroid.response.covertData
import com.walle.playandroid.ui.AppContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun isNetworkConnected(): Boolean {
    val cm = AppContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val info = cm.activeNetworkInfo
    if (null != info && info.isConnected()) {
        return info.getState() == NetworkInfo.State.CONNECTED
    }
    return false
}

open class BaseRepository {
    inline fun <T> safeCall(crossinline call: suspend () -> NetResponse<T>): Flow<DataResponse<T>> {
        return flow {
            emit(
                if (!isNetworkConnected()) DataResponse.Error(ErrorType.NET_ERROR_CONNECT, "网络未连接") else {
                    try {
                        covertData(call())
                    } catch (e: Throwable) {
                        DataResponse.Error(ErrorType.ERROR, e.localizedMessage, error = e)

                    }
                }
            )
        }

    }
}