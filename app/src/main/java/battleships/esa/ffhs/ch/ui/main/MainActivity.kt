package battleships.esa.ffhs.ch.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.ui.drawable.Game
import kotlinx.android.synthetic.main.main_activity.*

// https://material.io/resources/icons/?icon=directions_boat&style=baseline

class MainActivity : AppCompatActivity() {

    companion object {
        var isFirstLogin: Boolean =
            true        // temporary global variable to check if user is logged in for the first time
        var strictOverlapRule =
            true            // positioned ships are valid when there is space of 1 cell between ships if true; if false, no extra gap is needed
        var activeGame: Game? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.main_activity)

            val navController = Navigation.findNavController(
                this,
                R.id.nav_host_fragment
            )

            setupActionBar(navController)

        } catch (e: Exception) {
            println(e.stackTrace)
        }
    }

    // ----------------------------- navigation -----------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )
        return navController.navigateUp()
    }

    private fun setupActionBar(navController: NavController) {
        NavigationUI.setupActionBarWithNavController(this, navController, container)
    }

}
