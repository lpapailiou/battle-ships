package ch.ffhs.esa.battleships.di.boardpreparation

import ch.ffhs.esa.battleships.ui.boardpreparation.BoardPreparationFragment
import dagger.Subcomponent

@Subcomponent(modules = [BoardPreparationModule::class])
interface BoardPreparationComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BoardPreparationComponent
    }

    fun inject(fragment: BoardPreparationFragment)
}
