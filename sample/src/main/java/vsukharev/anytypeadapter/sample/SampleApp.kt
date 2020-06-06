package vsukharev.anytypeadapter.sample

import android.app.Application
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router
import javax.inject.Inject

class SampleApp : Application() {
    @Inject
    lateinit var cicerone: Cicerone<Router>

    override fun onCreate() {
        super.onCreate()
        Injector.buildAppComponent(this)
            .inject(this)
    }
}