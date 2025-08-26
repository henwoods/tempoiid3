package com.oiid.core.model.ui

import kotlinx.serialization.Serializable

@Serializable
data class TabItem(
    val route: String,
    val title: String,
    val asset: String,
    val type: String,
    val data: String? = null,
)
