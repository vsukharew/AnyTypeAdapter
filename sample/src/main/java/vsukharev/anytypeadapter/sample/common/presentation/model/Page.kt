package vsukharev.anytypeadapter.sample.common.presentation.model

/**
 * A portion of data
 * @param T - raw data that is gotten from some data source
 * @param R - common type for all the data that is displayed in the UI e.g. data items themselves, progress bar, ad blocks
 */
data class Page<out T, out R>(
    val rawData: List<T>,
    val uiData: List<R>
)

fun <T, R> emptyPage() = Page(emptyList<T>(), emptyList<R>())