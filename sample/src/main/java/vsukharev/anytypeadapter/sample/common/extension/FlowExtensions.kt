package vsukharev.anytypeadapter.sample.common.extension

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop

fun <T> Flow<T>.dropFirst() : Flow<T> = drop(1)