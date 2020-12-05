package vsukharev.anytypeadapter.sample.feed.data

import vsukharev.anytypeadapter.sample.feed.di.FeedScope
import vsukharev.anytypeadapter.sample.feed.domain.model.Feed
import vsukharev.anytypeadapter.sample.common.network.makeSimpleRequest
import javax.inject.Inject

@FeedScope
class FeedRepository @Inject constructor() {

    suspend fun getFeed(): Feed {
        makeSimpleRequest() // make request so that if there's no internet the error will be displayed
        return Feed(
            albums = AlbumsSource.albums,
            editorsChoice = EditorsChoiceSource.editorsChoiceMusic,
            activities = ActivitiesSource.activities
        )
    }
}