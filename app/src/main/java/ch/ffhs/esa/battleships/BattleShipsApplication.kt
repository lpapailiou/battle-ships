package ch.ffhs.esa.battleships

import android.app.Application
import ch.ffhs.esa.battleships.di.AppComponent
import ch.ffhs.esa.battleships.di.DaggerAppComponent

class BattleShipsApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

}
