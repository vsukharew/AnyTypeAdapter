package vsukharev.anytypeadapter.sample.common.presentation.delegate

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import java.util.*

/**
 * Data describing a view with image and text
 */
class IconWithTextAdapterItem(
    @DrawableRes val imageResId: Int,
    @StringRes val textResId: Int,
    val id: String = UUID.randomUUID().toString()
)