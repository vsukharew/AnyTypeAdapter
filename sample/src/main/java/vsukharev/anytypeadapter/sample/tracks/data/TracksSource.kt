package vsukharev.anytypeadapter.sample.tracks.data

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TracksSource @Inject constructor() {
    private val tracks = mutableListOf<Track>()

    fun getTracks(offset: Int, count: Int) = when {
            offset + count > tracks.size -> emptyList()
            else -> tracks.subList(offset, offset + count).toList()
        }

    fun putTracks(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }
}