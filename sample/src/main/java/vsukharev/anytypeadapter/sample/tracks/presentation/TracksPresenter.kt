package vsukharev.anytypeadapter.sample.tracks.presentation

import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.common.errorhandling.map
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.common.presentation.view.recyclerview.Paginator
import vsukharev.anytypeadapter.sample.tracks.domain.interactor.TracksInteractor
import vsukharev.anytypeadapter.sample.tracks.presentation.model.TracksListItem
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksView
import javax.inject.Inject

private const val PAGE_SIZE = 20

@InjectViewState
class TracksPresenter @Inject constructor(
    private val tracksInteractor: TracksInteractor
) : BasePresenter<TracksView>() {

    private var lastHeader = ""

    private val paginator = Paginator(
        { page, searchString ->
            tracksInteractor.getTracks(page * PAGE_SIZE, PAGE_SIZE, searchString)
                .map { tracks ->
                    val groupedTracks = tracks.groupBy { it.name.first().toString() }
                    mutableListOf<TracksListItem>().apply {
                        groupedTracks.forEach { (header, tracks) ->
                            if (lastHeader != header) {
                                add(TracksListItem.Header(header).also { lastHeader = it.value })
                            }
                            tracks.forEach { add(TracksListItem.TrackUi(it)) }
                        }
                    }
                }
        },
        viewState
    )

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        paginator.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        paginator.cancelLoad()
    }

    fun loadMore() {
        paginator.loadMore()
    }

    fun refresh() {
        paginator.refresh()
    }

    fun search(searchString: String) {
        paginator.search(searchString)
    }
}