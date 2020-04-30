package com.education.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import timber.log.Timber

class WifiSecurityChecker {
    companion object {
        fun setupListener(appContext: Context, callback: () -> Unit) {
            val cm = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            cm.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build(),
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        Timber.d("onAvailable(network)")
                        var isWEP = false
                        val wifi = appContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
                        val wifiInfo = wifi.connectionInfo
                        val networkList = wifi.scanResults
                        val currentSSID = wifiInfo.ssid
                        val currentNetwork = networkList.find { scanResult -> currentSSID == scanResult.SSID }
                        currentNetwork?.let {
                            isWEP = it.capabilities.contains("WEP")
                        }
                        if (isWEP)
                            callback.invoke()
                    }
                })
        }
    }
}