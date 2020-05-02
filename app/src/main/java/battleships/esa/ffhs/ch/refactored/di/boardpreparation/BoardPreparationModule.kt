package battleships.esa.ffhs.ch.refactored.di.boardpreparation

import androidx.lifecycle.ViewModel
import battleships.esa.ffhs.ch.refactored.business.boardpreparation.BoardPreparationViewModel
import battleships.esa.ffhs.ch.refactored.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BoardPreparationModule {

    @Binds
    @IntoMap
    @ViewModelKey(BoardPreparationViewModel::class)
    abstract fun bindViewModel(viewModel: BoardPreparationViewModel): ViewModel


}
