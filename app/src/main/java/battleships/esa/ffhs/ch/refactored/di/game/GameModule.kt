package battleships.esa.ffhs.ch.refactored.di.game

import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.refactored.di.ViewModelKey
import battleships.esa.ffhs.ch.refactored.business.game.GameViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class GameModule {

    @Binds
    @IntoMap
    @ViewModelKey(GameViewModel::class)
    abstract fun bindViewModel(viewModel: GameViewModel): ViewModel
}
