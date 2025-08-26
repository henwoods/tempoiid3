package com.oiid.core.datastore

import com.oiid.core.model.DarkThemeConfig
import com.oiid.core.model.ThemeBrand
import com.oiid.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val userData: Flow<UserData>

    // Theme Brand Operations
    suspend fun setThemeBrand(themeBrand: ThemeBrand): Result<Unit>
    suspend fun getThemeBrand(): Result<ThemeBrand>
    fun observeThemeBrand(): Flow<ThemeBrand>

    // Dark Theme Configuration Operations
    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit>
    suspend fun getDarkThemeConfig(): Result<DarkThemeConfig>
    fun observeDarkThemeConfig(): Flow<DarkThemeConfig>

    // Dynamic Color Preference Operations
    suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit>
    suspend fun getDynamicColorPreference(): Result<Boolean>
    fun observeDynamicColorPreference(): Flow<Boolean>

    suspend fun onboardingComplete(): Result<Unit>
    fun observeOnboardingComplete(): Flow<Boolean?>

    // User ID Operations
    suspend fun setUserId(userId: String): Result<Unit>
    suspend fun getUserId(): Result<String>
    fun observeUserId(): Flow<String>

    // Liked Posts Operations
    suspend fun addLikedPost(postId: String): Result<Unit>
    suspend fun removeLikedPost(postId: String): Result<Unit>
    suspend fun isPostLiked(postId: String): Result<Boolean>
    suspend fun getLikedPosts(): Result<Set<String>>

    // Liked Comments Operations
    suspend fun addLikedComment(commentId: String): Result<Unit>
    suspend fun removeLikedComment(commentId: String): Result<Unit>
    suspend fun isCommentLiked(commentId: String): Result<Boolean>
    suspend fun getLikedComments(): Result<Set<String>>

    // Batch Operations
    suspend fun resetToDefaults(): Result<Unit>
    suspend fun exportPreferences(): Result<UserData>
    suspend fun importPreferences(userData: UserData): Result<Unit>
}
