package battleships.esa.ffhs.ch.refactored.di.game

import battleships.esa.ffhs.ch.refactored.ui.game.GameFragment
import dagger.Subcomponent

@Subcomponent(modules = [GameModule::class])
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): GameComponent
    }

    fun inject(fragment: GameFragment)
}
