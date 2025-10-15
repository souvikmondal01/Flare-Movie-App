package com.souvikmondal01.flare.util

import android.os.SystemClock
import android.util.Log

object Extension {
    fun logD(msg: Any, x: String = "") {
        Log.d("SSSS", "$x - $msg")
    }

    fun startTime() = SystemClock.elapsedRealtime()
    fun Long.endTime(msg: String = "time") {
        val elapsed = SystemClock.elapsedRealtime() - this
        logD("$msg - $elapsed")
    }
}