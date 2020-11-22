package vsukharev.anytypeadapter.sample.feed.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.feed.data.FeedRepository
import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor.ShuffleMode.*
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import javax.inject.Inject
import kotlin.random.Random

private const val ALBUMS_PORTION = 5

/**
 * TODO edit comment
 * Interactor of the albums feature
 */
@FeedScope
class FeedInteractor @Inject constructor(private val repo: FeedRepository) {
    private var originalFeed: Feed? = null
    private lateinit var shuffledFeed: Feed
    private var activitiesTakenAfterLastShuffle: Int = Int.MAX_VALUE

    suspend fun getFeed(shuffleMode: ShuffleMode = NONE): Result<Feed> {
        return originalFeed?.let {
            shuffledFeed = when (shuffleMode) {
                ALBUM_COVERS -> it.shuffleAlbums().takeActivities(activitiesTakenAfterLastShuffle).takeAlbumsPortion()
                ACTIVITIES -> it.shuffleActivities().takeActivities().takeAlbumsPortion()
                else -> it
            }
            activitiesTakenAfterLastShuffle = shuffledFeed.activities.size
            Result.Success(shuffledFeed)
        }

//        originalFeed?.let {
//            shuffledFeed = when(shuffleMode) {
//                ALBUM_COVERS -> shuffledFeed?.shuffleAlbums() ?: it.shuffleAlbums()
//                ACTIVITIES -> {
//                    when {
//                        else -> it.shuffleActivities().takeActivities()
////                        shuffledFeed != null -> sh.shuffleActivities().takeActivities()
//                    }
//                     ?: it.shuffleActivities().takeActivities()
//                }
//                NONE -> shuffledFeed ?: it
//            }
//            Result.Success(shuffledFeed!!.takeAlbumsPortion())
//        }
            ?: try {
            repo.getFeed()
                .shuffleAlbums()
                .also {
                    originalFeed = it
                    shuffledFeed = it.takeAlbumsPortion()
                }
                .let { Result.Success(shuffledFeed) }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    enum class ShuffleMode {
        ALBUM_COVERS,
        ACTIVITIES,
        NONE
    }

    private fun Feed.shuffleAlbums(): Feed = copy(albums = albums.shuffled())

    private fun Feed.shuffleActivities(): Feed = copy(activities = activities.shuffled())

    private fun Feed.takeActivities(n: Int = Random(System.currentTimeMillis()).nextInt(0,2)): Feed =
        copy(activities = activities.take(n))

    private fun Feed.takeAlbumsPortion(portionSize: Int = ALBUMS_PORTION): Feed =
        copy(albums = albums.take(portionSize))
}