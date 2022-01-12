package com.walle.playandroid.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.walle.playandroid.response.DataResponse
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val _showLoading = MutableLiveData(false)
    val showLogin: LiveData<Boolean> = _showLoading


    fun <T> launchWithLoading(
        call: suspend () -> Flow<DataResponse<T>>,
        onSuccess: (DataResponse.Success<T>) -> Unit, isShowLogin:Boolean=true
    ) {
        viewModelScope.launch {
            call().onStart {
                if (isShowLogin) _showLoading.value = true

            }.onCompletion {
                if (isShowLogin) _showLoading.value = false
            }.catch {
                Toast.makeText(getApplication(), it.localizedMessage, Toast.LENGTH_SHORT).show()
            }.collectLatest {
                when (it) {
                    is DataResponse.Success -> {
                        onSuccess.invoke(it)
                    }
                    is DataResponse.Error -> {
                        Toast.makeText(getApplication(), it.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

}