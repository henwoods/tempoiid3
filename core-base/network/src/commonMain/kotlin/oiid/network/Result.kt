package oiid.network

/**
 * Represents the result of a network or remote operation, encapsulating either a success or an error.
 *
 * This is a sealed interface with two implementations:
 * - [Success] indicates the operation completed successfully and contains the resulting data.
 * - [Error] represents a failure and contains a [RemoteError] describing the error condition.
 *
 * @param D The type of data returned on success.
 * @param E The type of error returned on failure, constrained to [RemoteError].
 */
sealed interface Result<out D, out E : RemoteError> {

    /**
     * Represents a successful result.
     *
     * @param D The type of the successful response data.
     * @property data The actual result of the operation.
     */
    data class Success<out D>(val data: D) : Result<D, Nothing>

    /**
     * Represents a failed result due to a [RemoteError].
     *
     * @param E The specific type of [RemoteError] encountered.
     * @property error Details about the error that occurred.
     */
    data class Error<out E : RemoteError>(val error: E) : Result<Nothing, E>
}
