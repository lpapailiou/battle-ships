package battleships.esa.ffhs.ch.utils

import android.app.Application
import battleships.esa.ffhs.ch.data.GameMockRepository
import battleships.esa.ffhs.ch.data.MockDatabase
import battleships.esa.ffhs.ch.ui.viewmodel.ViewModelFactory

object InjectorUtils {

    fun provideGameViewModelFactory(): ViewModelFactory {
        val gameRepository =
            GameMockRepository.getInstance(
                MockDatabase.getInstance().gameListDao
            )
        return ViewModelFactory(gameRepository, application = Application())
    }
}