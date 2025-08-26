package com.oiid.core.data.di

import android.content.Context
import com.oiid.core.data.util.ConnectivityManagerNetworkMonitor
import com.oiid.core.data.utils.NetworkMonitor
import kotlinx.coroutines.CoroutineDispatcher

class AndroidPlatformDependentDataModule(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher,
) : PlatformDependentDataModule {
    override val networkMonitor: NetworkMonitor by lazy {
        ConnectivityManagerNetworkMonitor(context, dispatcher)
    }
}
