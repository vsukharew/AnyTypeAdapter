package vsukharev.anytypeadapter.sample.albums.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.albums.data.EditorsChoiceRepository
import vsukharev.anytypeadapter.sample.albums.di.AlbumsScope
import vsukharev.anytypeadapter.sample.albums.domain.model.EditorsChoice
import javax.inject.Inject

@AlbumsScope
class EditorsChoiceInteractor @Inject constructor(private val repository: EditorsChoiceRepository) {
    private var editorsChoice: List<EditorsChoice>? = null

    suspend fun getEditorsChoice(): Result<List<EditorsChoice>> {
        return editorsChoice?.let { Result.Success(it) } ?: try {
            Result.Success(repository.getAlbumsOftenListenedTo()).also { editorsChoice = it.data }
        } catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}