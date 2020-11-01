package vsukharev.anytypeadapter.sample.feed.di

import dagger.Component
import vsukharev.anytypeadapter.sample.feed.domain.interactor.FeedInteractor

@Component
@FeedScope
interface FeedComponent {
    fun albumsInteractor(): FeedInteractor
}