package vsukharev.anytypeadapter.sample.common.extension

import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator

/**
 * these functions convert one state to the other
 */

fun <T> Paginator.State<T>.toEmpty(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Empty<T> =
    Paginator.State.Empty(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toEmptyProgress(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.EmptyProgress<T> =
    Paginator.State.EmptyProgress(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toRefreshing(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Refreshing<T> =
    Paginator.State.Refreshing(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toNewPageLoading(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.NewPageLoading<T> =
    Paginator.State.NewPageLoading(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toData(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Data<T> =
    Paginator.State.Data(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toAllData(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.AllData<T> =
    Paginator.State.AllData(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toEmptyError(
    error: Throwable,
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.EmptyError<T> =
    Paginator.State.EmptyError(
        error,
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T> Paginator.State<T>.toPaginationError(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: List<T> = this.data,
    searchResults: List<T> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.PaginationError<T> =
    Paginator.State.PaginationError(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

