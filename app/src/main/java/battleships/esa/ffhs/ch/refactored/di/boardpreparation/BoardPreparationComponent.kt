package battleships.esa.ffhs.ch.refactored.di.boardpreparation

import battleships.esa.ffhs.ch.refactored.ui.boardpreparation.BoardPreparationFragment
import dagger.Subcomponent

@Subcomponent(modules = [BoardPreparationModule::class])
interface BoardPreparationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BoardPreparationComponent
    }

    fun inject(fragment: BoardPreparationFragment)
}
