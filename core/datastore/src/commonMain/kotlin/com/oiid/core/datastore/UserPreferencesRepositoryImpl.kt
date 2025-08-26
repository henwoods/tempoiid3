package com.oiid.core.datastore

import com.oiid.core.model.DarkThemeConfig
import com.oiid.core.model.ThemeBrand
import com.oiid.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import oiid.core.base.datastore.repository.ReactivePreferencesRepository

/**
 * Implementation of UserPreferencesRepository using the reactive datastore.
 *
 * This implementation leverages the reactive capabilities of the datastore to provide
 * real-time updates to preferences and maintains a combined userData flow.
 */
class UserPreferencesRepositoryImpl(
    private val preferencesStore: ReactivePreferencesRepository,
) : UserPreferencesRepository {

    companion object {
        private const val THEME_BRAND_KEY = "theme_brand"
        private const val DARK_THEME_CONFIG_KEY = "dark_theme_config"
        private const val DYNAMIC_COLOR_KEY = "use_dynamic_color"
        private const val ONBOARDING_COMPLETE = "onboarding_complete"
        private const val USER_ID_KEY = "user_id"
        private const val LIKED_POSTS_KEY = "liked_posts"
        private const val LIKED_COMMENTS_KEY = "liked_comments"

        // Default values
        private val DEFAULT_THEME_BRAND = ThemeBrand.DEFAULT
        private val DEFAULT_DARK_THEME_CONFIG = DarkThemeConfig.FOLLOW_SYSTEM
        private const val DEFAULT_DYNAMIC_COLOR = false
        private const val DEFAULT_USER_ID = ""
        private const val DEFAULT_LIKED_POSTS = ""
        private const val DEFAULT_LIKED_COMMENTS = ""
    }

    /**
     * Reactive userData flow that combines all user preferences.
     * Automatically updates whenever any preference changes.
     */
    override val userData: Flow<UserData> = combine(
        observeThemeBrand(),
        observeDarkThemeConfig(),
        observeDynamicColorPreference(),
        observeUserId(),
    ) { themeBrand, darkThemeConfig, useDynamicColor, userId ->
        UserData(
            themeBrand = themeBrand,
            darkThemeConfig = darkThemeConfig,
            useDynamicColor = useDynamicColor,
            userId = userId,
        )
    }.distinctUntilChanged()

    // Theme Brand Operations
    override suspend fun setThemeBrand(themeBrand: ThemeBrand): Result<Unit> {
        return preferencesStore.savePreference(THEME_BRAND_KEY, themeBrand.brandName)
    }

    override suspend fun getThemeBrand(): Result<ThemeBrand> {
        return preferencesStore.getPreference(THEME_BRAND_KEY, DEFAULT_THEME_BRAND.brandName)
            .map { brandName -> ThemeBrand.fromString(brandName) }
    }

    override fun observeThemeBrand(): Flow<ThemeBrand> {
        return preferencesStore.observePreference(THEME_BRAND_KEY, DEFAULT_THEME_BRAND.brandName)
            .map { brandName -> ThemeBrand.fromString(brandName) }
    }

    // Dark Theme Configuration Operations
    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig): Result<Unit> {
        return preferencesStore.savePreference(DARK_THEME_CONFIG_KEY, darkThemeConfig.name)
    }

    override suspend fun getDarkThemeConfig(): Result<DarkThemeConfig> {
        return preferencesStore.getPreference(DARK_THEME_CONFIG_KEY, DEFAULT_DARK_THEME_CONFIG.name)
            .map { configName -> DarkThemeConfig.fromString(configName) }
    }

    override fun observeDarkThemeConfig(): Flow<DarkThemeConfig> {
        return preferencesStore.observePreference(
            DARK_THEME_CONFIG_KEY,
            DEFAULT_DARK_THEME_CONFIG.name,
        )
            .map { configName -> DarkThemeConfig.fromString(configName) }
    }

    // Dynamic Color Preference Operations
    override suspend fun setDynamicColorPreference(useDynamicColor: Boolean): Result<Unit> {
        return preferencesStore.savePreference(DYNAMIC_COLOR_KEY, useDynamicColor)
    }

    override suspend fun getDynamicColorPreference(): Result<Boolean> {
        return preferencesStore.getPreference(DYNAMIC_COLOR_KEY, DEFAULT_DYNAMIC_COLOR)
    }

    override fun observeDynamicColorPreference(): Flow<Boolean> {
        return preferencesStore.observePreference(DYNAMIC_COLOR_KEY, DEFAULT_DYNAMIC_COLOR)
    }

    // Complete flags
    override suspend fun onboardingComplete(): Result<Unit> {
        return preferencesStore.savePreference(ONBOARDING_COMPLETE, true)
    }

    override fun observeOnboardingComplete(): Flow<Boolean?> =
        preferencesStore.observePreference<Boolean?>(ONBOARDING_COMPLETE, false)
            .onStart { emit(null) }

    // User ID Operations
    override suspend fun setUserId(userId: String): Result<Unit> {
        return preferencesStore.savePreference(USER_ID_KEY, userId)
    }

    override suspend fun getUserId(): Result<String> {
        return preferencesStore.getPreference(USER_ID_KEY, DEFAULT_USER_ID)
    }

    override fun observeUserId(): Flow<String> {
        return preferencesStore.observePreference(USER_ID_KEY, DEFAULT_USER_ID)
    }

    // Batch Operations
    override suspend fun resetToDefaults(): Result<Unit> {
        return runCatching {
            setThemeBrand(DEFAULT_THEME_BRAND).getOrThrow()
            setDarkThemeConfig(DEFAULT_DARK_THEME_CONFIG).getOrThrow()
            setDynamicColorPreference(DEFAULT_DYNAMIC_COLOR).getOrThrow()
            setUserId(DEFAULT_USER_ID).getOrThrow()
        }
    }

    override suspend fun exportPreferences(): Result<UserData> {
        return runCatching {
            UserData(
                themeBrand = getThemeBrand().getOrThrow(),
                darkThemeConfig = getDarkThemeConfig().getOrThrow(),
                useDynamicColor = getDynamicColorPreference().getOrThrow(),
                userId = getUserId().getOrThrow(),
            )
        }
    }

    override suspend fun importPreferences(userData: UserData): Result<Unit> {
        return runCatching {
            setThemeBrand(userData.themeBrand).getOrThrow()
            setDarkThemeConfig(userData.darkThemeConfig).getOrThrow()
            setDynamicColorPreference(userData.useDynamicColor).getOrThrow()
            setUserId(userData.userId).getOrThrow()
        }
    }

    override suspend fun addLikedPost(postId: String): Result<Unit> {
        val currentLikedPosts = getLikedPosts().getOrDefault(emptySet())
        val updatedLikedPosts = currentLikedPosts + postId

        return preferencesStore.savePreference(LIKED_POSTS_KEY, updatedLikedPosts.toCsv())
    }

    override suspend fun removeLikedPost(postId: String): Result<Unit> {
        val currentLikedPosts = getLikedPosts().getOrDefault(emptySet())
        val updatedLikedPosts = currentLikedPosts - postId
        return preferencesStore.savePreference(LIKED_POSTS_KEY, updatedLikedPosts.toCsv())
    }

    override suspend fun isPostLiked(postId: String): Result<Boolean> {
        return preferencesStore.getPreference(LIKED_POSTS_KEY, DEFAULT_LIKED_POSTS).map { likedPosts ->
            likedPosts.contains(postId)
        }
    }

    override suspend fun getLikedPosts(): Result<Set<String>> {
        return preferencesStore.getPreference(LIKED_POSTS_KEY, DEFAULT_LIKED_POSTS).map { it.toStringSet() }
    }

    // Liked Comments Operations
    override suspend fun addLikedComment(commentId: String): Result<Unit> {
        val currentLikedComments = getLikedComments().getOrDefault(emptySet())
        val updatedLikedComments = currentLikedComments + commentId
        return preferencesStore.savePreference(LIKED_COMMENTS_KEY, updatedLikedComments.toCsv())
    }

    override suspend fun removeLikedComment(commentId: String): Result<Unit> {
        val currentLikedComments = getLikedComments().getOrDefault(emptySet())
        val updatedLikedComments = currentLikedComments - commentId
        return preferencesStore.savePreference(LIKED_COMMENTS_KEY, updatedLikedComments.toCsv())
    }

    override suspend fun isCommentLiked(commentId: String): Result<Boolean> {
        return preferencesStore.getPreference(LIKED_COMMENTS_KEY, DEFAULT_LIKED_COMMENTS).map { likedComments ->
            likedComments.contains(commentId)
        }
    }

    override suspend fun getLikedComments(): Result<Set<String>> {
        return preferencesStore.getPreference(LIKED_COMMENTS_KEY, DEFAULT_LIKED_COMMENTS).map { it.toStringSet() }
    }

    fun Set<String>.toCsv(): String = joinToString(",")

    fun String.toStringSet(): Set<String> =
        split(",").mapNotNull { it.trim().takeIf { it.isNotEmpty() } }.toSet()
}
