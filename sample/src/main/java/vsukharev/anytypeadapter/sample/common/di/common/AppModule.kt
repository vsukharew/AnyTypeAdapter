package vsukharev.anytypeadapter.sample.common.di.common

import dagger.Module
import dagger.Provides
import ru.terrakok.cicerone.Router
import javax.inject.Singleton

@Module
object AppModule {
    @JvmStatic
    @Provides
    @Singleton
    fun provideRouter(): Router = Router()
}