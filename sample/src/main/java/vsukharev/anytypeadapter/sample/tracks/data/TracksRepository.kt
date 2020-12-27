package vsukharev.anytypeadapter.sample.tracks.data

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TracksRepository @Inject constructor(
    private val tracksSource: TracksSource
) {
    fun getTracks(): List<Track> {
        return with(tracksSource) {
            putTracks(FakeExternalTracksSource.tracks)
            getTracks()
        }
    }
}