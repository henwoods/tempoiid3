package oiid.core.base.platform.review

/**
 * Manages application review requests across platforms.
 *
 * This interface abstracts the platform-specific implementations for requesting
 * user reviews of the application. It provides methods to prompt users for app
 * reviews using either standard system-provided flows or custom-designed review
 * experiences.
 *
 * Platform-specific implementations of this interface typically integrate with:
 * - Google Play In-App Review API on Android
 * - StoreKit Review Controller on iOS
 * - Other platform-specific review mechanisms
 *
 * Review requests should be triggered at appropriate moments in the user journey
 * when the user has completed a meaningful interaction with the application and
 * is likely to have a positive experience to report.
 */
interface AppReviewManager {

    /**
     * Prompts the user to review the app using the platform's standard review flow.
     *
     * This method triggers the native system review prompt, which is typically
     * managed by the platform to control frequency and prevent review fatigue.
     * The actual display of the review prompt may be deferred or throttled by
     * the platform based on internal policies.
     */
    fun promptForReview()

    /**
     * Prompts the user to review the app using a custom application-defined review flow.
     *
     * This method initiates a custom review experience designed within the application,
     * which may include custom UI elements, multi-step processes, or conditional
     * logic before directing users to the appropriate store page for leaving a review.
     *
     * Custom review flows provide more control over the user experience but require
     * careful implementation to comply with platform guidelines and avoid potential
     * rejection during app review processes.
     */
    fun promptForCustomReview()
}
