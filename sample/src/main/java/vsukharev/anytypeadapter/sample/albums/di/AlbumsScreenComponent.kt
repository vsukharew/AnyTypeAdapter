package vsukharev.anytypeadapter.sample.albums.di

import dagger.Component
import vsukharev.anytypeadapter.sample.albums.presentation.view.AlbumsFragment
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen

@Component(dependencies = [AlbumsComponent::class])
@PerScreen
interface AlbumsScreenComponent {
    fun inject(fragment: AlbumsFragment)
}