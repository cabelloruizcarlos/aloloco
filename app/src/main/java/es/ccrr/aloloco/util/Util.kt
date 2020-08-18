package es.ccrr.aloloco.util

import android.content.Context
import android.graphics.Point
import android.net.ConnectivityManager
import android.view.WindowManager
import es.ccrr.aloloco.ui.base.BaseActivity


class Util {


    private fun getScreenPoint(windowManager: WindowManager): Point? {

        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size
    }

    fun getScreenWidth(windowManager: WindowManager): Int {
        return getScreenPoint(windowManager)?.let {
            it.x
        } ?: run { 0 }
    }


    companion object {

        fun freeMemory() {
            System.runFinalization()
            Runtime.getRuntime().gc()
            System.gc()
        }

        fun isOnline(context: Context): Boolean {

            val netInfo =
                (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo
            // If the device is in Flight Mode netInfo will be null
            return netInfo != null && netInfo.isConnected
        }
    }
}