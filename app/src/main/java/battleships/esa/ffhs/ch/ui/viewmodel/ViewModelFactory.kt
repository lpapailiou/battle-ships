package battleships.esa.ffhs.ch.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.entity.GameRepository

class ViewModelFactory(private val gameRepository: GameRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(gameRepository) as T
    }
}