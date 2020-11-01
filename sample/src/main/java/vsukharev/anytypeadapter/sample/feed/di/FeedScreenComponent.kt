package vsukharev.anytypeadapter.sample.feed.di

import dagger.Component
import vsukharev.anytypeadapter.sample.feed.presentation.view.FeedFragment
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen

@Component(dependencies = [FeedComponent::class])
@PerScreen
interface FeedScreenComponent {
    fun inject(fragment: FeedFragment)
}