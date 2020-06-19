package ch.ffhs.esa.battleships.ui.notification

import android.app.AlertDialog
import android.content.Context
import ch.ffhs.esa.battleships.R

class CustomDialog {

    fun showEndGameDialog(context: Context, youWin: Int) {
        var dialog: AlertDialog.Builder = AlertDialog.Builder(context, R.style.AppDialogTheme)
        dialog.setTitle("THE WAR IS OVER")
        if (youWin == 1) {
            dialog.setMessage("You are the best general!")
        } else {
            dialog.setMessage("Your wife will be very mad at you.")
        }
        dialog.show()
    }

}
