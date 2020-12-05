package vsukharev.anytypeadapter.sample.feed.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.feed.data.FeedRepository
import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor.ShuffleMode.*
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import javax.inject.Inject
import kotlin.random.Random

/**
 * TODO edit comment
 * Interactor of the albums feature
 */
@FeedScope
class FeedInteractor @Inject constructor(private val repo: FeedRepository) {
    private var feed: Feed? = null
    private lateinit var shuffledFeed: Feed
    private lateinit var shuffledFeedPrint: Feed

    suspend fun getFeed(shuffleMode: ShuffleMode = NONE): Result<Feed> {
        return feed?.let {
            shuffledFeed = shuffledFeed.shuffle(shuffleMode)
            shuffledFeedPrint = shuffledFeed.makePrint(shuffleMode)
            Result.Success(shuffledFeedPrint)
        } ?: try {
            repo.getFeed()
                .also {
                    feed = it
                    shuffledFeed = it.shuffle()
                    shuffledFeedPrint = shuffledFeed.makePrint(shuffleMode)
                }
                .let { Result.Success(shuffledFeedPrint) }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    enum class ShuffleMode {
        ALBUM_COVERS,
        ACTIVITIES,
        NONE
    }

    private fun Feed.shuffle(shuffleMode: ShuffleMode = NONE): Feed = copy(
        albums = albums.shuffledOrThis { shuffleMode == ALBUM_COVERS },
        activities = activities.shuffledOrThis { shuffleMode == ACTIVITIES }
    )

    private fun <T> List<T>.shuffledOrThis(predicate: () -> Boolean): List<T> {
        return when {
            predicate.invoke() -> shuffled()
            else -> this
        }
    }

    private fun Feed.makePrint(shuffleMode: ShuffleMode): Feed {
        val feed = when (shuffleMode) {
            ACTIVITIES -> takeActivities()
            else -> this
        }
        return feed.takeAlbumsPortion()
    }

    private fun Feed.takeActivities(
        n: Int = Random(System.currentTimeMillis()).nextInt(
            from = 0,
            until = 9
        )
    ): Feed = copy(activities = activities.take(n))

    private fun Feed.takeAlbumsPortion(portionSize: Int = ALBUMS_PORTION): Feed =
        copy(albums = albums.take(portionSize))

    private companion object {
        private const val ALBUMS_PORTION = 5
    }
}