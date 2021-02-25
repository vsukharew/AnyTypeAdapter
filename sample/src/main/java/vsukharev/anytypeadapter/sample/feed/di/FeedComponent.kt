package vsukharev.anytypeadapter.sample.feed.di

import dagger.Component
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import javax.inject.Singleton

@Component
@Singleton
interface FeedComponent {
    fun inject(fragment: FeedFragment)
}