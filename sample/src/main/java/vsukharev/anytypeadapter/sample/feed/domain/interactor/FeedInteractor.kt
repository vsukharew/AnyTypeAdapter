package vsukharev.anytypeadapter.sample.feed.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.feed.data.FeedRepository
import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import javax.inject.Inject

/**
 * TODO edit comment
 * Interactor of the albums feature
 */
@FeedScope
class FeedInteractor @Inject constructor(private val repo: FeedRepository) {

    fun getFeed(isStaticInterface: Boolean): Result<Feed> {
        return try {
            Result.Success(repo.getFeed(isStaticInterface))
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}