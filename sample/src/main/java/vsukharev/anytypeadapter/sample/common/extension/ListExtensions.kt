package vsukharev.anytypeadapter.sample.common.extension

fun <T> List<T>.doIfEmpty(action: () -> Unit) {
    if (isEmpty()) action.invoke()
}