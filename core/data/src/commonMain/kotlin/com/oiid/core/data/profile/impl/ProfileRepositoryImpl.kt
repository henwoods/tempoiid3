package com.oiid.core.data.profile.impl

import com.oiid.core.data.profile.ProfileRepository
import com.oiid.core.data.profile.ProfileService
import com.oiid.core.model.Profile
import com.oiid.core.model.SignedURLResponse
import com.oiid.core.model.api.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProfileRepositoryImpl(
    private val profileService: ProfileService,
) : ProfileRepository {
    
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    private val _currentUserProfile = MutableStateFlow<Profile?>(null)
    override val currentUserProfile: StateFlow<Profile?> = _currentUserProfile.asStateFlow()
    
    init {
        profileService.getCurrentUserProfile()
            .onEach { resource ->
                _currentUserProfile.value = when (resource) {
                    is Resource.Success -> resource.data
                    else -> null
                }
            }
            .launchIn(scope)
    }
    
    override suspend fun loadCurrentUserProfile(userId: String) {
        profileService.loadUserProfile(userId)
    }
    
    override suspend fun updateProfile(
        userId: String,
        name: String?,
        bio: String?,
        headerImageURL: String?,
        profileImageURL: String?,
    ): Profile {
        return profileService.updateProfile(userId, name, bio, headerImageURL, profileImageURL)
    }
    
    override suspend fun getProfileImageUploadUrl(): SignedURLResponse {
        return profileService.getProfileImageUploadUrl()
    }
    
    override suspend fun getHeaderImageUploadUrl(): SignedURLResponse {
        return profileService.getHeaderImageUploadUrl()
    }

    override fun clearProfile() {
        profileService.clearProfile()
    }
}