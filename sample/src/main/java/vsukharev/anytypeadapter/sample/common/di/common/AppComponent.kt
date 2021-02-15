package vsukharev.anytypeadapter.sample.common.di.common

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import vsukharev.anytypeadapter.sample.SampleApp
import javax.inject.Singleton

@Component
@Singleton
interface AppComponent {
    fun inject(app: SampleApp)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }
}