package com.oiid.core.data.ui

import com.oiid.core.model.ui.TabItem
import oiid.core.base.platform.context.AppContext

expect class TabRepository {
    constructor(context: AppContext)

    suspend fun getTabItems(): List<TabItem>
}
