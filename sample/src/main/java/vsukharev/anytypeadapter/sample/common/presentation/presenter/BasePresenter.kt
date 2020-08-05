package vsukharev.anytypeadapter.sample.common.presentation.presenter

import kotlinx.coroutines.*
import moxy.MvpPresenter
import vsukharev.anytypeadapter.sample.common.presentation.view.BaseView
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class BasePresenter<V: BaseView> : MvpPresenter<V>() {
    private val jobs = mutableListOf<Job>()

    override fun onDestroy() {
        super.onDestroy()
        jobs.cancel()
    }

    protected fun Job.cancelIfActive() {
        if (isActive) {
            cancel()
        }
    }

    protected fun startJobOnMain(block: suspend CoroutineScope.() -> Unit): Job {
        return startJob(Dispatchers.Main, block = block)
    }

    private fun startJob(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job {
        return GlobalScope.launch(context, start, block).also { jobs.add(it) }
    }

    private fun List<Job>.cancel() {
        forEach { it.cancel() }
    }
}