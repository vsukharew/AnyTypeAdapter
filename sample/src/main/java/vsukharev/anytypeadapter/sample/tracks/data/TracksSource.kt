package vsukharev.anytypeadapter.sample.tracks.data

import vsukharev.anytypeadapter.sample.tracks.domain.model.Track
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.min

@Singleton
class TracksSource @Inject constructor() {
    private val tracks = mutableListOf<Track>()

    val isEmpty: Boolean
    get() = tracks.isEmpty()

    fun getTracks(offset: Int, count: Int, searchString: String? = null): List<Track> {
        return when {
            searchString != null -> {
                val searchResult = tracks
                    .filter { it.name.contains(searchString, ignoreCase = true) }
                    .takeIf { offset <= it.size }
                searchResult
                    ?.subList(offset, min(offset + count, searchResult.size))
                    ?.toList() ?: emptyList()
            }
            else -> {
                tracks
                    .takeIf { offset <= it.size }
                    ?.subList(offset, min(offset + count, tracks.size))
                    ?.toList() ?: emptyList()
            }
        }
    }

    fun putTracks(tracks: List<Track>) {
        this.tracks.clear()
        this.tracks.addAll(tracks)
    }
}