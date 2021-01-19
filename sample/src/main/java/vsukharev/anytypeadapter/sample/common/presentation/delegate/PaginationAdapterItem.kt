package vsukharev.anytypeadapter.sample.common.presentation.delegate

import java.util.*

data class PaginationAdapterItem(
    val isError: Boolean,
    val id: UUID = UUID.randomUUID()
)