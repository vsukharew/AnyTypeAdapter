package vsukharev.anytypeadapter.sample.tracks.presentation

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import moxy.InjectViewState
import vsukharev.anytypeadapter.sample.common.di.common.PerScreen
import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.common.presentation.presenter.BasePresenter
import vsukharev.anytypeadapter.sample.tracks.domain.interactor.TracksInteractor
import vsukharev.anytypeadapter.sample.tracks.presentation.view.TracksView
import javax.inject.Inject

@InjectViewState
@PerScreen
class TracksPresenter @Inject constructor(
    private val tracksInteractor: TracksInteractor
) : BasePresenter<TracksView>() {

    private lateinit var getTracksJob: Job

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        getTracksJob = startJobOnMain {
            viewState.showProgress()
            delay(3000)
            viewState.hideProgress()
            when (val result = tracksInteractor.getTracks()) {
                is Result.Success -> viewState.showTracks(result.data)
                else -> {}
            }

        }
    }
}