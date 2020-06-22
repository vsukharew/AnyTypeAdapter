package vsukharev.anytypeadapter.sample

import android.content.Context
import vsukharev.anytypeadapter.sample.albums.di.AlbumsComponent
import vsukharev.anytypeadapter.sample.albums.di.AlbumsScreenComponent
import vsukharev.anytypeadapter.sample.albums.di.DaggerAlbumsComponent
import vsukharev.anytypeadapter.sample.albums.di.DaggerAlbumsScreenComponent
import vsukharev.anytypeadapter.sample.common.di.common.AppComponent
import vsukharev.anytypeadapter.sample.common.di.common.DaggerAppComponent

/**
 * Class containing methods for building and destroying Dagger components
 */
object Injector {
    private var appComponent: AppComponent? = null
    private var albumsComponent: AlbumsComponent? = null
    private var albumsScreenComponent: AlbumsScreenComponent? = null

    fun buildAppComponent(context: Context): AppComponent {
        return DaggerAppComponent.factory()
            .create(context)
            .also { appComponent = it }
    }

    private fun buildAlbumsComponent(): AlbumsComponent {
        return albumsComponent ?: DaggerAlbumsComponent.builder()
            .build()
            .also { albumsComponent = it }
    }

    fun buildAlbumsScreenComponent(): AlbumsScreenComponent {
        return albumsScreenComponent ?: DaggerAlbumsScreenComponent.builder()
            .albumsComponent(buildAlbumsComponent())
            .build()
            .also { albumsScreenComponent = it }
    }

    fun destroyAlbumsComponent() {
        if (albumsScreenComponent == null) {
            albumsComponent = null
        }
    }

    fun destroyAlbumsScreenComponent() {
        albumsScreenComponent = null
        destroyAlbumsComponent()
    }
}