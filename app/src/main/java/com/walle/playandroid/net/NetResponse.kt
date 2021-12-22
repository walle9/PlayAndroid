package com.walle.playandroid.net

data class NetResponse<T>(val errorCode:Int = 0,val errorMsg:String,val data:T)
