package com.myapplication.base

import android.util.Log

object MyLogs {

    fun addLog(logType: LogEnums.LogType?, TAG: String?, logMessage: String) {
        when (logType) {
            LogEnums.LogType.MyError -> Log.e(TAG, logMessage) // Error log
            LogEnums.LogType.MyWarnings -> Log.w(TAG, logMessage) // Warning log
            LogEnums.LogType.MyInfo -> Log.i(TAG, logMessage) // Info log
            LogEnums.LogType.MyDebug -> Log.d(TAG, logMessage) // Debug log
            LogEnums.LogType.MyVerbos -> Log.v(TAG, logMessage) // Verbo log
            else -> {}
        }
    }

}