package vsukharev.anytypeadapter.sample.tracks.presentation.view

import moxy.viewstate.strategy.alias.AddToEndSingle
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView
import vsukharev.anytypeadapter.sample.tracks.domain.model.Track

interface TracksView : ErrorHandlerView {
    @AddToEndSingle
    fun showProgress()

    @AddToEndSingle
    fun hideProgress()

    @AddToEndSingle
    fun showTracks(tracks: List<Track>)

    @AddToEndSingle
    fun showEmptyState()
}