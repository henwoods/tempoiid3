package com.oiid.core.common

import com.oiid.core.common.CMPBuildConfig.BASE_URL_DEV
import com.oiid.core.common.CMPBuildConfig.BASE_URL_PROD
import com.oiid.core.common.CMPBuildConfig.CLIENT_ID_DEV
import com.oiid.core.common.CMPBuildConfig.CLIENT_ID_PROD
import com.oiid.core.common.CMPBuildConfig.DEBUG_MODE
import com.oiid.core.common.CMPBuildConfig.HOST_URL_DEV
import com.oiid.core.common.CMPBuildConfig.HOST_URL_PROD
import com.oiid.core.common.CMPBuildConfig.REGION_DEV
import com.oiid.core.common.CMPBuildConfig.REGION_PROD
import com.oiid.core.common.CMPBuildConfig.X_API_KEY_DEV
import com.oiid.core.common.CMPBuildConfig.X_API_KEY_PROD

sealed class APIConfig(
    val xApiKey: String,
    val baseUrl: String,
    val hostUrl: String,
    val region: String,
    val clientId: String,
) {
    companion object {
        fun apiConfig(): APIConfig {
            return if (DEBUG_MODE) {
                DevAPIConfig
            } else {
                ProdAPIConfig
            }
        }
    }
    object ProdAPIConfig : APIConfig(
        xApiKey = X_API_KEY_PROD,
        baseUrl = BASE_URL_PROD,
        hostUrl = HOST_URL_PROD,
        region = REGION_PROD,
        clientId = CLIENT_ID_PROD,
    )

    object DevAPIConfig : APIConfig(
        xApiKey = X_API_KEY_DEV,
        baseUrl = BASE_URL_DEV,
        hostUrl = HOST_URL_DEV,
        region = REGION_DEV,
        clientId = CLIENT_ID_DEV,
    )
}
