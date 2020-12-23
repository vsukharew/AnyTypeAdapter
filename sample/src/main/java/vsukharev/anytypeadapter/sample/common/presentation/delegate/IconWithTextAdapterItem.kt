package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.*

/**
 * Data describing a view with image and text
 */
class IconWithTextAdapterItem(
    val id: String,
    val text: String,
    @DrawableRes val imageResId: Int
)