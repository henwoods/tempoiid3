package com.oiid.core.data.profile

import com.oiid.core.model.Profile
import com.oiid.core.model.SignedURLResponse
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val currentUserProfile: StateFlow<Profile?>
    
    suspend fun loadCurrentUserProfile(userId: String)
    
    suspend fun updateProfile(
        userId: String,
        name: String? = null,
        bio: String? = null,
        headerImageURL: String? = null,
        profileImageURL: String? = null,
    ): Profile
    
    suspend fun getProfileImageUploadUrl(): SignedURLResponse
    
    suspend fun getHeaderImageUploadUrl(): SignedURLResponse
}