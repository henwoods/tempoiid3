package com.oiid.network.api

import com.oiid.core.model.ProfileResponse
import com.oiid.core.model.SignedURLResponse
import com.oiid.core.model.UpdateProfileRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

/**
 * API service for profile-related operations.
 */
interface ProfileApiService {
    /**
     * Fetches a user profile by ID.
     * @param profileId The unique identifier for the user profile.
     * @return A [ProfileResponse] object.
     */
    @GET("app/users/profiles/{profileId}")
    suspend fun getProfile(@Path("profileId") profileId: String): ProfileResponse

    /**
     * Updates the current user's profile.
     * @param updateProfileRequest The profile data to update.
     * @return The updated [UpdateProfileRequest].
     */
    @PUT("app/users/profiles")
    suspend fun updateProfile(@Body updateProfileRequest: UpdateProfileRequest): ProfileResponse

    /**
     * Fetches a presigned URL for uploading a profile image.
     * @return A [SignedURLResponse] containing the signed URL and public URL.
     */
    @GET("app/users/profile/newImage")
    suspend fun getProfileImageUploadUrl(): SignedURLResponse

    /**
     * Fetches a presigned URL for uploading a header image.
     * @return A [SignedURLResponse] containing the signed URL and public URL.
     */
    @GET("app/users/profile/newHeaderImage")
    suspend fun getHeaderImageUploadUrl(): SignedURLResponse
}
