package vsukharev.anytypeadapter.sample.tracks.di

import dagger.Component
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksFragment

@Component
@PerScreen
interface TracksComponent {
    fun inject(fragment: TracksFragment)
}