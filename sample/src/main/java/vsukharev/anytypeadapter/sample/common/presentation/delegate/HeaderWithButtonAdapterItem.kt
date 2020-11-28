package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.StringRes
import java.util.*

/**
 * Data describing the view that contains a text and button
 */
class HeaderWithButtonAdapterItem(
    @StringRes val headerTextResId: Int,
    @StringRes val btnTextResId: Int,
    val onClickListener: (() -> Unit)? = null,
    val id: String = UUID.randomUUID().toString()
)