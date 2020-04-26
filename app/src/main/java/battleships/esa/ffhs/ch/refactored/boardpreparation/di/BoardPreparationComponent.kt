package battleships.esa.ffhs.ch.refactored.boardpreparation.di

import battleships.esa.ffhs.ch.refactored.boardpreparation.BoardPreparationFragment
import dagger.Subcomponent

@Subcomponent(modules = [BoardPreparationModule::class])
interface BoardPreparationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BoardPreparationComponent
    }

    fun inject(fragment: BoardPreparationFragment)
}
