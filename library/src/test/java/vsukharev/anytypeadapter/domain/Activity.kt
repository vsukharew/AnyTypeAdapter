package vsukharev.anytypeadapter.domain

import java.util.*

data class Activity(
    val name: String = UUID.randomUUID().toString(),
    val id: String = UUID.randomUUID().toString()
)