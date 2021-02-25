package vsukharev.anytypeadapter.sample.tracks.presentation.model

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track

sealed class TracksListItem {
    data class TrackUi(val track: Track) : TracksListItem()
    data class Header(val value: String) : TracksListItem()
}