package vsukharev.anytypeadapter.sample

import android.content.Context
import vsukharev.anytypeadapter.sample.common.di.common.AppComponent
import vsukharev.anytypeadapter.sample.common.di.common.DaggerAppComponent

/**
 * Class containing methods for building and destroying Dagger components
 */
object Injector {
    private var appComponent: AppComponent? = null

    fun buildAppComponent(context: Context): AppComponent {
        return DaggerAppComponent.factory()
            .create(context)
            .also { appComponent = it }
    }
}