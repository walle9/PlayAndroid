package com.walle.playandroid.response

data class NetResponse<T>(val errorCode:Int = 0,val errorMsg:String,val data:T)
