package com.oiid.core.data.ui

import com.oiid.core.config.R
import com.oiid.core.model.ui.TabItem
import kotlinx.serialization.json.Json
import oiid.core.base.platform.context.AppContext

actual class TabRepository {
    val appContext: AppContext
    val json: Json = Json { ignoreUnknownKeys = true }

    actual constructor(context: AppContext) {
        appContext = context
    }

    actual suspend fun getTabItems(): List<TabItem> {
        val resourceId = R.raw.tabs
        val jsonString = appContext.resources.openRawResource(resourceId).bufferedReader().use { it.readText() }

        return json.decodeFromString<List<TabItem>>(jsonString)
    }
}
