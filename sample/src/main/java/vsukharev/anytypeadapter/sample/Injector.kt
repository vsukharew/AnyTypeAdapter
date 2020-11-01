package vsukharev.anytypeadapter.sample

import android.content.Context
import vsukharev.anytypeadapter.sample.feed.di.FeedComponent
import vsukharev.anytypeadapter.sample.feed.di.FeedScreenComponent
import vsukharev.anytypeadapter.sample.feed.di.DaggerFeedComponent
import vsukharev.anytypeadapter.sample.feed.di.DaggerFeedScreenComponent
import vsukharev.anytypeadapter.sample.common.di.common.AppComponent
import vsukharev.anytypeadapter.sample.common.di.common.DaggerAppComponent

/**
 * Class containing methods for building and destroying Dagger components
 */
object Injector {
    private var appComponent: AppComponent? = null
    private var feedComponent: FeedComponent? = null
    private var feedScreenComponent: FeedScreenComponent? = null

    fun buildAppComponent(context: Context): AppComponent {
        return DaggerAppComponent.factory()
            .create(context)
            .also { appComponent = it }
    }

    private fun buildFeedComponent(): FeedComponent {
        return feedComponent ?: DaggerFeedComponent.builder()
            .build()
            .also { feedComponent = it }
    }

    fun buildAlbumsScreenComponent(): FeedScreenComponent {
        return feedScreenComponent ?: DaggerFeedScreenComponent.builder()
            .feedComponent(buildFeedComponent())
            .build()
            .also { feedScreenComponent = it }
    }

    fun destroyAlbumsComponent() {
        if (feedScreenComponent == null) {
            feedComponent = null
        }
    }

    fun destroyAlbumsScreenComponent() {
        feedScreenComponent = null
        destroyAlbumsComponent()
    }
}