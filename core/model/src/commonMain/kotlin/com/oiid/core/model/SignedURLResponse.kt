package com.oiid.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignedURLResponse(
    @SerialName("signed_url")
    val signedUrl: String,
    val url: String,
)
