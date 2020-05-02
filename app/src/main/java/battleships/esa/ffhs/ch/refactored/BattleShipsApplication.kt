package battleships.esa.ffhs.ch.refactored

import android.app.Application
import battleships.esa.ffhs.ch.refactored.di.AppComponent
import battleships.esa.ffhs.ch.refactored.di.DaggerAppComponent


open class BattleShipsApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
