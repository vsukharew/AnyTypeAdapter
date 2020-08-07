package vsukharev.anytypeadapter.sample

import android.app.Application
import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import vsukharev.anytypeadapter.sample.common.presentation.view.ErrorHandlerView

abstract class BaseActivity : MvpAppCompatActivity(), ErrorHandlerView {
    protected val app: Application by lazy { application as SampleApp }

    override fun showNoInternetError(e: Throwable) {
        showErrorMessage(R.string.no_internet_error_title_text)
    }

    override fun showUnknownError(e: Throwable) {
        showErrorMessage(R.string.unknown_error_text)
    }

    override fun hideError() {}

    private fun showErrorMessage(@StringRes message: Int) {
        val view = findViewById<View>(android.R.id.content)
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
            .setAnchorView(main_bottom_navigation_view)
            .show()
    }
}