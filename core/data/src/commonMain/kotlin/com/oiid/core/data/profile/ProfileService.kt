package com.oiid.core.data.profile

import com.oiid.core.model.Profile
import com.oiid.core.model.SignedURLResponse
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.flow.Flow

interface ProfileService {
    suspend fun loadUserProfile(userId: String)

    fun getCurrentUserProfile(): Flow<Resource<Profile>>

    fun getProfile(profileId: String): Flow<Resource<Profile>>

    suspend fun updateProfile(
        userId: String,
        name: String? = null,
        bio: String? = null,
        headerImageURL: String? = null,
        profileImageURL: String? = null,
    ): Profile

    /**
     * Fetches a presigned URL for uploading a profile image.
     * @return A [SignedURLResponse] containing the signed URL and public URL.
     */
    suspend fun getProfileImageUploadUrl(): SignedURLResponse

    /**
     * Fetches a presigned URL for uploading a header image.
     * @return A [SignedURLResponse] containing the signed URL and public URL.
     */
    suspend fun getHeaderImageUploadUrl(): SignedURLResponse
}
