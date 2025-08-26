package com.oiid.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.oiid.core.data.auth.AuthRepository
import com.oiid.core.data.profile.ProfileService
import com.oiid.core.data.user.UserRepository
import com.oiid.core.datastore.UserPreferencesRepository
import com.oiid.core.model.DarkThemeConfig
import com.oiid.core.model.SignedURLResponse
import com.oiid.core.model.ThemeBrand
import com.oiid.core.model.api.Resource
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.extension
import io.github.vinceglb.filekit.readBytes
import io.ktor.client.HttpClient
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel(
    private val settingsRepository: UserPreferencesRepository,
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val profileService: ProfileService,
) : ViewModel(), KoinComponent {

    private val httpClient: HttpClient by inject()

    private val _loggingOut = MutableStateFlow(false)
    val loggingOut: StateFlow<Boolean> = _loggingOut

    private val _deletingAccount = MutableStateFlow(false)
    val deletingAccount: StateFlow<Boolean> = _deletingAccount

    private val _editProfileState = MutableStateFlow(EditProfileUiState(isEditing = false))
    val editProfileState: StateFlow<EditProfileUiState> = _editProfileState

    val settingsUiState: StateFlow<SettingsUiState> = settingsRepository.userData.map { userDate ->
        SettingsUiState.Success(
            settings = UserEditableSettings(
                brand = userDate.themeBrand,
                useDynamicColor = userDate.useDynamicColor,
                darkThemeConfig = userDate.darkThemeConfig,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsUiState.Loading,
    )

    val profileUiState: StateFlow<ProfileUiState> = profileService.getCurrentUserProfile().map { resource ->
        when (resource) {
            is Resource.Loading -> ProfileUiState.Loading
            is Resource.Error -> ProfileUiState.Error(resource.exception.message ?: "Unknown error")
            is Resource.Success -> {
                if (!this::userId.isInitialized) {
                    Logger.d("No used id yet")
                    ProfileUiState.Loading
                } else {
                    val profileData = ProfileData(
                        profileImageUrl = resource.data.profileImage,
                        headerImageUrl = resource.data.headerImage,
                        name = resource.data.name ?: "",
                        bio = resource.data.bio ?: "",
                        isCurrentUser = resource.data.isCurrentUser,
                    )

                    ProfileUiState.Success(profileData)
                }
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileUiState.Loading,
    )

    lateinit var userId: String

    init {
        viewModelScope.launch {
            userId = ensureUserId()
            if (userId.isNotBlank()) {
                profileService.loadUserProfile(userId)
            } else {
                Logger.w("User ID is blank — profile not loaded")
            }
        }
    }

    private suspend fun ensureUserId(): String {
        val cached = userRepository.getUserId()
        if (!cached.isNullOrBlank()) {
            settingsRepository.setUserId(cached)
            return cached
        }

        return when (val result = userRepository.fetchAndStoreUserInfo()) {
            is Resource.Success -> {
                val userId = result.data.userId
                settingsRepository.setUserId(userId)
                userId
            }

            else -> ""
        }
    }

    fun updateThemeBrand(themeBrand: ThemeBrand) {
        viewModelScope.launch {
            settingsRepository.setThemeBrand(themeBrand)
        }
    }

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            settingsRepository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDynamicColorPreference(useDynamicColor)
        }
    }

    fun saveProfileChanges() {
        viewModelScope.launch {
            _editProfileState.update { it.copy(isLoading = true) }
            val currentName = _editProfileState.value.editedProfileData?.name
            val currentBio = _editProfileState.value.editedProfileData?.bio
            val currentHeaderImageUrl = _editProfileState.value.editedProfileData?.headerImageUrl

            try {
                profileService.updateProfile(
                    userId = userId,
                    name = currentName,
                    bio = currentBio,
                    headerImageURL = currentHeaderImageUrl,
                )

                exitEditMode()
            } catch (e: Exception) {
                Logger.e(e) { "Error updating profile ${e.message}" }
                exitEditMode()
            }
        }
    }

    fun onEditProfileClick() {
        viewModelScope.launch {
            if (profileUiState.value is ProfileUiState.Success) {
                val data = (profileUiState.value as ProfileUiState.Success).profileData
                val editingProfileData = EditProfileUiState(data, true)
                _editProfileState.update { editingProfileData }
            }
        }
    }

    private fun exitEditMode() {
        _editProfileState.update { it.copy(isEditing = false, isLoading = false) }
    }

    fun updateEditableName(name: String) {
        val updatedProfile = _editProfileState.value.editedProfileData?.copy(name = name)
        _editProfileState.update { it.copy(updatedProfile) }
    }

    fun updateEditableBio(bio: String) {
        val updatedProfile = _editProfileState.value.editedProfileData?.copy(bio = bio)
        _editProfileState.update { it.copy(updatedProfile) }
    }

    fun cancelEditing() {
        exitEditMode()
    }

    fun onDeleteAccount(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            _deletingAccount.value = true
            authRepository.deleteAccount()
            onLogout {
                onLogoutComplete()
            }
            _deletingAccount.value = false
        }
    }

    fun onLogout(onLogoutComplete: () -> Unit) {
        viewModelScope.launch {
            _loggingOut.value = true
            authRepository.signOut()

            userRepository.clearUserInfo()
            // Clear userId from preferences as well
            settingsRepository.setUserId("")

            onLogoutComplete()
            _loggingOut.value = false
        }
    }

    fun onImagePicked(type: String, file: PlatformFile) {
        viewModelScope.launch {
            val currentName = _editProfileState.value.editedProfileData?.name ?: ""
            val currentBio = _editProfileState.value.editedProfileData?.bio ?: ""
            val currentHeaderImageUrl = _editProfileState.value.editedProfileData?.headerImageUrl
            val currentProfileImageUrl = _editProfileState.value.editedProfileData?.profileImageUrl

            try {
                _editProfileState.update { it.copy(isLoading = true) }

                val isProfileImage = type == "profile"

                val signedURLResponse = if (isProfileImage) {
                    profileService.getProfileImageUploadUrl()
                } else {
                    profileService.getHeaderImageUploadUrl()
                }

                val success = uploadImageToPresignedUrl(file, signedURLResponse)

                if (success) {
                    val profileImageUrl = if (isProfileImage) signedURLResponse.url else currentProfileImageUrl
                    val headerImageUrl = if (!isProfileImage) signedURLResponse.url else currentHeaderImageUrl
                    val updatedProfile = _editProfileState.value.editedProfileData?.copy(
                        profileImageUrl = profileImageUrl,
                        headerImageUrl = headerImageUrl,
                        name = currentName,
                        bio = currentBio,
                    )

                    profileService.updateProfile(
                        userId = userId,
                        profileImageURL = profileImageUrl,
                        headerImageURL = headerImageUrl,
                        name = currentName,
                        bio = currentBio,
                    )

                    _editProfileState.update { it.copy(editedProfileData = updatedProfile, isLoading = false) }
                } else {
                    _editProfileState.update { it.copy(isLoading = false) }
                    Logger.e("Failed to upload image")
                }
            } catch (e: Exception) {
                _editProfileState.update { it.copy(isLoading = false) }
                Logger.e(e) { "Error uploading image: ${e.message}" }
            }
        }
    }

    private suspend fun uploadImageToPresignedUrl(
        file: PlatformFile,
        signedURLResponse: SignedURLResponse,
    ): Boolean = runCatching {
        val bytes = file.readBytes()

        val contentType = when (file.extension.lowercase()) {
            "png" -> ContentType.Image.PNG
            "jpg", "jpeg" -> ContentType.Image.JPEG
            "webp" -> ContentType("image", "webp")
            else -> ContentType.Application.OctetStream
        }

        httpClient.put(signedURLResponse.signedUrl) {
            contentType(contentType)
            setBody(bytes)
        }
    }.onFailure { e ->
        Logger.e(e) { "Error uploading image: ${e.message}" }
    }.isSuccess
}

data class UserEditableSettings(
    val brand: ThemeBrand,
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)

sealed interface SettingsUiState {
    data object Loading : SettingsUiState
    data class Success(val settings: UserEditableSettings) : SettingsUiState
}

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Error(val message: String) : ProfileUiState
    data class Success(val profileData: ProfileData) : ProfileUiState
}

data class ProfileData(
    val profileImageUrl: String? = null,
    val headerImageUrl: String? = null,
    val name: String = "",
    val bio: String = "",
    val isCurrentUser: Boolean = false,
)

data class EditProfileUiState(
    val editedProfileData: ProfileData? = null,
    val isEditing: Boolean,
    val isLoading: Boolean = false,
)
