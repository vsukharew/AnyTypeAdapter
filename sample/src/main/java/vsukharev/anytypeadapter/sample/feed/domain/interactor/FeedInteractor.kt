package vsukharev.anytypeadapter.sample.feed.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.feed.data.FeedRepository
import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.model.Album
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import javax.inject.Inject

private const val ALBUMS_PORTION = 5

/**
 * TODO edit comment
 * Interactor of the albums feature
 */
@FeedScope
class FeedInteractor @Inject constructor(private val repo: FeedRepository) {

    private var feed: Feed? = null

    suspend fun getFeed(): Result<Feed> {
        return feed?.let { Result.Success(it.copyWithRandomAlbums(ALBUMS_PORTION)) } ?: try {
            repo.getFeed()
                .also { feed = it }
                .let { Result.Success(it.copyWithRandomAlbums(ALBUMS_PORTION)) }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }

    private fun Feed.copyWithRandomAlbums(albumsAmount: Int): Feed =
        copy(albums = albums.takeRandomItems(albumsAmount))

    private fun List<Album>.takeRandomItems(n: Int): List<Album> = shuffled().take(n)
}