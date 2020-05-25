package vsukharev.anytypeadapter.sample

import android.app.Application

class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Injector.buildAppComponent(this)
            .inject(this)
    }
}