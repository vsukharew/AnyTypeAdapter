package vsukharev.anytypeadapter.sample.tracks.di

import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksFragment
import javax.inject.Singleton

@Component
@Singleton
interface TracksComponent {
    @ExperimentalCoroutinesApi
    fun inject(fragment: TracksFragment)
}