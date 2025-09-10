package com.oiid.core.data.profile.impl

import co.touchlab.kermit.Logger
import com.oiid.core.data.profile.ProfileService
import com.oiid.core.model.Profile
import com.oiid.core.model.SignedURLResponse
import com.oiid.core.model.UpdateProfileRequest
import com.oiid.core.model.api.Resource
import com.oiid.core.model.toProfile
import com.oiid.network.api.ProfileApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import oiid.core.base.datastore.cache.CacheManager
import oiid.core.base.datastore.cache.LruCacheManager

private const val CURRENT_USER_PROFILE_CACHE_KEY = "PROFILE_CACHE_KEY"

class ProfileServiceImpl(
    private val profileApiService: ProfileApiService,
    private val profileCache: CacheManager<String, Profile> = LruCacheManager(),
) : ProfileService {
    companion object {
        const val TAG = "ProfileServiceImpl"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val currentUserProfileFlow = MutableStateFlow<Resource<Profile>>(Resource.Loading)

    override suspend fun loadUserProfile(userId: String) {
        val cachedProfile = profileCache.get("${CURRENT_USER_PROFILE_CACHE_KEY}_$userId")
        if (cachedProfile != null) {
            currentUserProfileFlow.value = Resource.Success(cachedProfile)
        } else {
            currentUserProfileFlow.value = Resource.Loading
        }

        try {
            val profile = profileApiService.getProfile(userId).toProfile(isCurrentUser = true)
            updateCache("${CURRENT_USER_PROFILE_CACHE_KEY}_$userId", profile)

            Logger.d("$TAG cached profile ${profile.name}")
            currentUserProfileFlow.value = Resource.Success(profile)
        } catch (e: Exception) {
            Logger.e("$TAG Error loading current user profile: ${e.message}")
            if (cachedProfile != null) {
                Logger.d { "$TAG Network failed. Serving stale data from cache" }
                currentUserProfileFlow.value = Resource.Success(cachedProfile)
            } else {
                Logger.w { "$TAG Network failed and cache is empty. Emitting error state." }
                currentUserProfileFlow.value = Resource.Error(e)
            }
        }
    }

    override fun getCurrentUserProfile(): Flow<Resource<Profile>> {
        return currentUserProfileFlow
    }

    override fun getProfile(profileId: String): Flow<Resource<Profile>> {
        val profileFlow = MutableStateFlow<Resource<Profile>>(Resource.Loading)
        val cacheKey = "${CURRENT_USER_PROFILE_CACHE_KEY}_$profileId"

        val cachedProfile = profileCache.get(cacheKey)
        if (cachedProfile != null) {
            profileFlow.value = Resource.Success(cachedProfile)
        }

        coroutineScope.launch {
            try {
                val profile = profileApiService.getProfile(profileId).toProfile()
                updateCache(cacheKey, profile)
                profileFlow.value = Resource.Success(profile)
            } catch (e: Exception) {
                Logger.e("$TAG Error loading profile $profileId: ${e.message}")
                if (cachedProfile != null) {
                    Logger.d { "$TAG Network failed. Serving stale data from cache" }
                    profileFlow.value = Resource.Success(cachedProfile)
                } else {
                    Logger.w { "$TAG Network failed and cache is empty. Emitting error state." }
                    profileFlow.value = Resource.Error(e)
                }
            }
        }

        return profileFlow
    }

    override suspend fun updateProfile(
        userId: String,
        name: String?,
        bio: String?,
        headerImageURL: String?,
        profileImageURL: String?,
    ): Profile {
        val updateProfileRequest = UpdateProfileRequest(
            name = name,
            bio = bio,
            headerImage = headerImageURL,
            profileImage = profileImageURL,
        )

        val updatedProfileResponse = profileApiService.updateProfile(updateProfileRequest)
        val updatedProfile = updatedProfileResponse.toProfile(isCurrentUser = true)

        updateCache("${CURRENT_USER_PROFILE_CACHE_KEY}_$userId", updatedProfile)

        currentUserProfileFlow.value = Resource.Success(updatedProfile)

        return updatedProfile
    }

    override suspend fun getProfileImageUploadUrl(): SignedURLResponse {
        return try {
            profileApiService.getProfileImageUploadUrl()
        } catch (e: Exception) {
            Logger.e("$TAG Error getting profile image upload URL: ${e.message}")
            throw e
        }
    }

    override suspend fun getHeaderImageUploadUrl(): SignedURLResponse {
        return try {
            profileApiService.getHeaderImageUploadUrl()
        } catch (e: Exception) {
            Logger.e("$TAG Error getting header image upload URL: ${e.message}")
            throw e
        }
    }

    override fun clearProfile() {
        profileCache.clear()
    }

    private fun updateCache(key: String, profile: Profile) {
        profileCache.put(key, profile)
    }
}
