package vsukharev.anytypeadapter.sample.common.errorhandling

fun <T, R> Result<T>.map(mapper: (T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.Success(mapper.invoke(data))
    is Result.Failure -> Result.Failure(e)
}