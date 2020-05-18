package ch.ffhs.esa.battleships.di.boardpreparation

import androidx.lifecycle.ViewModel
import ch.ffhs.esa.battleships.business.boardpreparation.BoardPreparationViewModel
import ch.ffhs.esa.battleships.di.ViewModelKey
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
