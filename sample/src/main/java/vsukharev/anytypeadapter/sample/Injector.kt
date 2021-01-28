package vsukharev.anytypeadapter.sample

import android.content.Context
import vsukharev.anytypeadapter.sample.feed.di.FeedComponent
import vsukharev.anytypeadapter.sample.feed.di.DaggerFeedComponent
import vsukharev.anytypeadapter.sample.common.di.common.AppComponent
import vsukharev.anytypeadapter.sample.common.di.common.DaggerAppComponent
import vsukharev.anytypeadapter.sample.tracks.di.DaggerTracksComponent
import vsukharev.anytypeadapter.sample.tracks.di.TracksComponent

/**
 * Class containing methods for building and destroying Dagger components
 */
object Injector {
    private var appComponent: AppComponent? = null
    private var feedComponent: FeedComponent? = null
    private var tracksComponent: TracksComponent? = null

    fun buildAppComponent(context: Context): AppComponent {
        return DaggerAppComponent.factory()
            .create(context)
            .also { appComponent = it }
    }

    fun buildFeedComponent(): FeedComponent {
        return feedComponent ?: DaggerFeedComponent.builder()
            .build()
            .also { feedComponent = it }
    }

    fun destroyFeedComponent() {
        feedComponent = null
    }

    fun buildTracksComponent(): TracksComponent {
        return tracksComponent ?: DaggerTracksComponent.builder()
            .build()
            .also { tracksComponent = it }
    }

    fun destroyTracksComponent() {
        tracksComponent = null
    }
}