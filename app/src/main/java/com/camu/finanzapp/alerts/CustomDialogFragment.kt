package com.camu.finanzapp.alerts
import android.app.AlertDialog
import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.camu.finanzapp.R
import com.camu.finanzapp.login.LoginFragment


class CustomDialogFragment(private val title: String,private val message: String, private val isRegister: Boolean) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val alertDialog : AlertDialog

        if (isRegister){
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("Aceptar") { _, _ ->
                    val loginFragment = LoginFragment.newInstance()
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragmentContainerViewFinal, loginFragment)
                        .addToBackStack("LoginFragment")
                        .commit()
                }
                .create()
        }
        else{
            alertDialog = AlertDialog.Builder(requireContext())
                .setMessage(message)
                .setTitle(title)
                .setPositiveButton("Aceptar",null)
                .create()
        }



        // Aplicar el estilo personalizado al botón "Aceptar"
        alertDialog.setOnShowListener { dialog ->
            val button = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
            val resources: Resources = resources
            val colorButtonEnabled = resources.getColor(R.color.buttoncolor)
            button.setTextColor(colorButtonEnabled)
        }

        return alertDialog
    }
}