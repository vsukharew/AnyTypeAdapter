package vsukharev.anytypeadapter.sample.common.errorhandling

/**
 * The class that wraps any success or error response
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(var e: Throwable) : Result<Nothing>()
}