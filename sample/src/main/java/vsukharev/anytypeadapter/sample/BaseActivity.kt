package vsukharev.anytypeadapter.sample

import android.app.Application
import moxy.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity() {
    protected val app: Application by lazy { application as SampleApp }
}