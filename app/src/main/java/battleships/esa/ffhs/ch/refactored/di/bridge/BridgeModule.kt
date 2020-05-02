package battleships.esa.ffhs.ch.refactored.di.game

import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.refactored.business.bridge.BridgeViewModel
import battleships.esa.ffhs.ch.refactored.business.game.GameViewModel
import battleships.esa.ffhs.ch.refactored.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BridgeModule {

    @Binds
    @IntoMap
    @ViewModelKey(BridgeViewModel::class)
    abstract fun bindViewModel(viewModel: BridgeViewModel): ViewModel
}
