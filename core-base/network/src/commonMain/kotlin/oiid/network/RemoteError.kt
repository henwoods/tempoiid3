package oiid.network

/**
 * Represents standardized error types for remote or network operations.
 *
 * This enum is typically used with the [Result.Error] variant to describe what kind of failure occurred.
 */
enum class RemoteError {

    /**
     * The request was malformed or missing required parameters (HTTP 400).
     */
    BAD_REQUEST,

    /**
     * The requested resource could not be found (HTTP 404).
     */
    NOT_FOUND,

    /**
     * Authentication failed due to invalid or missing credentials (HTTP 401).
     */
    UNAUTHORIZED,

    /**
     * The request timed out, usually due to a slow or unresponsive network (HTTP 408 or socket timeout).
     */
    REQUEST_TIMEOUT,

    /**
     * The client has sent too many requests in a given amount of time (HTTP 429).
     */
    TOO_MANY_REQUESTS,

    /**
     * A server-side error occurred (HTTP 5xx).
     */
    SERVER,

    /**
     * The response could not be deserialized, likely due to mismatched or invalid data formats.
     */
    SERIALIZATION,

    /**
     * An unknown or unexpected error occurred, used as a fallback when the specific cause is not identifiable.
     */
    UNKNOWN,
}
