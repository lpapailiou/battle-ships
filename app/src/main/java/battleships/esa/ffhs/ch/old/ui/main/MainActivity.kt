package battleships.esa.ffhs.ch.old.ui.main

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import battleships.esa.ffhs.ch.R
import battleships.esa.ffhs.ch.old.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    companion object {
        var isFirstLogin: Boolean =
            false                        // temporary global variable to check if user is logged in for the first time
        lateinit var mainViewModel: MainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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

    // ----------------------------- hardware connector -----------------------------

    fun vibrate() {
        val vibrator = applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        200,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(200)
            }
        }
    }

}
