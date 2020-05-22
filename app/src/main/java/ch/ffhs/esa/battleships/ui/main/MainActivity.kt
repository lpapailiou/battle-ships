package ch.ffhs.esa.battleships.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import ch.ffhs.esa.battleships.R
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val navController = Navigation.findNavController(
            this,
            R.id.nav_host_fragment
        )

        setupActionBar(navController)

    }

    // ----------------------------- navigation -----------------------------

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val item: MenuItem? = menu?.getItem(2)
        if (item != null) {
//            mainViewModel.getCurrentGame().observe(this, Observer {           // remove 'current game' menu item if there is no current game
//                item.setVisible(it != null)
//                invalidateOptionsMenu()     // TODO: not clean yet, menu item is still visible when game was ended
//            })
        }
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

