package battleships.esa.ffhs.ch.entity

import battleships.esa.ffhs.ch.ui.viewmodel.ViewModelFactory

object InjectorUtils {

    fun provideGameViewModelFactory(): ViewModelFactory {
        val gameRepository = GameRepository.getInstance(MockDatabase.getInstance().gameListDao)
        return ViewModelFactory(gameRepository)
    }
}