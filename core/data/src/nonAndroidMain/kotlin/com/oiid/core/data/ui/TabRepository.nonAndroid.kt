package com.oiid.core.data.ui

import com.oiid.core.model.ui.TabItem
import oiid.core.base.platform.context.AppContext

actual class TabRepository {
    actual suspend fun getTabItems(): List<TabItem> {
        return listOf(TabItem("feed", "Feed", "", "feed"), TabItem("profile", "Profile", "", "profile"))
    }

    actual constructor(context: AppContext) {
        // Do nothing
    }
}
