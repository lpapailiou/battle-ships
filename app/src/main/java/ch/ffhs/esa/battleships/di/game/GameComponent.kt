package ch.ffhs.esa.battleships.di.game

import ch.ffhs.esa.battleships.ui.game.GameFragment
import dagger.Subcomponent

@Subcomponent(modules = [GameModule::class])
interface GameComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): GameComponent
    }

    fun inject(fragment: GameFragment)
}
