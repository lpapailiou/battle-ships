package battleships.esa.ffhs.ch.refactored

import android.app.Application
import battleships.esa.ffhs.ch.refactored.di.*

class BattleShipsApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}
