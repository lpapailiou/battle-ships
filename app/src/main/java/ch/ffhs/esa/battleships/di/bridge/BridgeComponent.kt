package ch.ffhs.esa.battleships.di.game

import ch.ffhs.esa.battleships.ui.bridge.BridgeFragment
import dagger.Subcomponent

@Subcomponent(modules = [BridgeModule::class])
interface BridgeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BridgeComponent
    }

    fun inject(fragment: BridgeFragment)
}
