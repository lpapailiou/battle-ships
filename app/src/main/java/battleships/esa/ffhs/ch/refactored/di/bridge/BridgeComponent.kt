package battleships.esa.ffhs.ch.refactored.di.game

import battleships.esa.ffhs.ch.old.ui.main.BridgeFragment
import dagger.Subcomponent

@Subcomponent(modules = [BridgeModule::class])
interface BridgeComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BridgeComponent
    }

    fun inject(fragment: BridgeFragment)
}
