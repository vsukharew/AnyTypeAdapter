package vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewScrollListener(
    private val onLoadMore: () -> Unit
) : RecyclerView.OnScrollListener() {
    var isLoading = false
    var hasMore = false
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        with(recyclerView) {
            val threshold = 5
            val layoutManager = layoutManager as LinearLayoutManager
            val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            val totalItemCount = layoutManager.itemCount
            if (!isLoading || hasMore) {
                if (lastVisibleItemPosition + threshold >= totalItemCount) {
                    isLoading = true
                    onLoadMore.invoke()
                }
            }
        }
    }
}