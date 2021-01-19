package vsukharev.anytypeadapter.sample.tracks.di

import dagger.Component
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksFragment
import javax.inject.Singleton

@Component
@Singleton
interface TracksComponent {
    fun inject(fragment: TracksFragment)
}