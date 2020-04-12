package battleships.esa.ffhs.ch.ui.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import battleships.esa.ffhs.ch.data.GameMockRepository

class ViewModelFactory(private val gameRepository: GameMockRepository, val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T: ViewModel?> create(modelClass: Class<T>): T {
        return GameListViewModel(gameRepository, application) as T
    }

}