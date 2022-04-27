package vsukharev.anytypeadapter.sample.common.extension

import vsukharev.anytypeadapter.sample.common.presentation.model.Page
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator

/**
 * these functions convert one state to the other
 */

fun <T, R> Paginator.State<T, R>.toEmpty(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Empty<T, R> =
    Paginator.State.Empty(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toEmptyProgress(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.EmptyProgress<T, R> =
    Paginator.State.EmptyProgress(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toRefreshing(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Refreshing<T, R> =
    Paginator.State.Refreshing(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toNewPageLoading(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.NewPageLoading<T, R> =
    Paginator.State.NewPageLoading(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toData(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.Data<T, R> =
    Paginator.State.Data(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toAllData(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.AllData<T, R> =
    Paginator.State.AllData(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toEmptyError(
    error: Throwable,
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.EmptyError<T, R> =
    Paginator.State.EmptyError(
        error,
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

fun <T, R> Paginator.State<T, R>.toPaginationError(
    currentPage: Int = this.currentPage,
    currentSearchPage: Int = this.currentSearchPage,
    data: Page<T, R> = this.data,
    searchResults: Page<T, R> = this.searchResults,
    searchString: String? = this.searchString,
): Paginator.State.PaginationError<T, R> =
    Paginator.State.PaginationError(
        currentPage,
        currentSearchPage,
        data,
        searchResults,
        searchString
    )

