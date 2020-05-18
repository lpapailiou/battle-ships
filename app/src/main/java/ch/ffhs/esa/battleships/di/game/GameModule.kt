package ch.ffhs.esa.battleships.di.game

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.di.ViewModelKey
import ch.ffhs.esa.battleships.business.game.GameViewModel
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
