package vsukharev.anytypeadapter.sample.albums.domain.interactor

import vsukharev.anytypeadapter.sample.common.errorhandling.Result
import vsukharev.anytypeadapter.sample.albums.data.EditorsChoiceRepository
import vsukharev.anytypeadapter.sample.albums.di.AlbumsScope
import vsukharev.anytypeadapter.sample.albums.domain.model.EditorsChoice
import javax.inject.Inject

@AlbumsScope
class EditorsChoiceInteractor @Inject constructor(private val repository: EditorsChoiceRepository) {

    suspend fun getEditorsChoice(): Result<List<EditorsChoice>> {
        return try {
            Result.Success(repository.getAlbumsOftenListenedTo())
        }
        catch (e: Throwable) {
            Result.Failure(e)
        }
    }
}