package ch.ffhs.esa.battleships.di.game

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.business.bridge.BridgeViewModel
import ch.ffhs.esa.battleships.di.ViewModelKey
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
