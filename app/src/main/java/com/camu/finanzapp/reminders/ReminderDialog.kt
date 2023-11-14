package com.camu.finanzapp.reminders


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.databasereminders.application.RemindersDBApp
import com.camu.finanzapp.databasereminders.data.ReminderRepository
import com.camu.finanzapp.databasereminders.data.db.ReminderDatabase
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity
import com.camu.finanzapp.databaseusers.data.DBRepository
import com.camu.finanzapp.databaseusers.data.db.DBDataBase
import com.camu.finanzapp.databinding.ReminderDialogBinding
import com.camu.finanzapp.util.ReminderItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale.Category

class ReminderDialog (
    private val newReminder: Boolean = true,
    private val reminder: ReminderEntity = ReminderEntity(
        title = "",
        category = "",
        date = "",
        hour = "",
        mount = "",
        userReminderId = ""
    ),
    private val updateUI: ()-> Unit,
    private val message: (String) -> Unit
): DialogFragment(){

    private var _binding: ReminderDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: ReminderRepository

    private var spinnerItemSelected: String = "Renta/Hipoteca"

    var isSpinnerOpen = false

    private lateinit var firebaseAuth: FirebaseAuth


    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ReminderDialogBinding.inflate(requireActivity().layoutInflater)

        val app = requireActivity().application as RemindersDBApp
        repository = app.repository

        builder = AlertDialog.Builder(requireContext())


        firebaseAuth = FirebaseAuth.getInstance()

        val singInAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireContext())




        var SpinnerCategory: Spinner
        SpinnerCategory = binding.SpinnerCategory

        val categoryItems = listOf(
            ReminderItem("Renta/Hipoteca", R.mipmap.ic_reminder_rent_foreground),
            ReminderItem("Luz/Electricidad", R.mipmap.ic_reminder_electricity_foreground),
            ReminderItem("Agua", R.mipmap.ic_reminder_water_foreground),
            ReminderItem("Internet/telefono", R.mipmap.ic_reminder_wifi_foreground),
            ReminderItem("Tv/Stream", R.mipmap.ic_reminder_tv_foreground),
            ReminderItem("Automotriz", R.mipmap.ic_reminder_car_foreground),
            ReminderItem("Tarjeta de credito", R.mipmap.ic_reminder_credit_card_foreground),
            ReminderItem("Otro", R.mipmap.ic_reminder_other_foreground)
        )

        val adapter = CategoryAdapter(requireContext(), categoryItems)
        adapter.setDropDownViewResource(R.layout.spinner_item_layout)
        SpinnerCategory.adapter = adapter

        SpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = categoryItems[position].text

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Renta/Hipoteca"

            }
        }
        binding.apply {
            editTextNameReminder.setText(reminder.title)
            editTextMountReminder.setText(reminder.mount)
            editTextDateReminder.setText(reminder.date)
            editTextHourReminder.setText(reminder.hour)
            editTextHourReminder.setText(reminder.mount)

            val indexToSelect = categoryItems.indexOfFirst { it.text == reminder.category }

            // Seleccion del elemento por su índice
            if (indexToSelect != -1) {
                SpinnerCategory.setSelection(indexToSelect)
            }

        }



        dialog = if (newReminder) {
            buildDialog("Guardar", "Cancelar", {
                //Create (Guardar)
                reminder.title = binding.editTextNameReminder.text.toString()
                reminder.category = spinnerItemSelected
                reminder.mount = binding.editTextMountReminder.text.toString()
                reminder.date = binding.editTextDateReminder.text.toString()
                reminder.hour = binding.editTextHourReminder.text.toString()




                try {
                    lifecycleScope.launch {
                        repository.insertReminder(reminder)
                    }
                    if (singInAccount!=null && firebaseAuth.currentUser!=null){
                        reminder.userReminderId = firebaseAuth.currentUser?.email.toString()
                    }else{
                        val database = DBDataBase.getDataBase(requireContext())
                        val repository = DBRepository(database.userDao())
                        val userEmail = getUserEmail()

                        lifecycleScope.launch {
                            val user = repository.getUserByEmail(userEmail)
                            reminder.userReminderId = user?.email.toString()
                        }
                    }

                    message("Recordatorio Guardado")

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el recordatorio")
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                //Update
                reminder.title = binding.editTextNameReminder.text.toString()
                reminder.category = spinnerItemSelected
                reminder.mount = binding.editTextMountReminder.text.toString()
                reminder.date = binding.editTextDateReminder.text.toString()
                reminder.hour = binding.editTextHourReminder.text.toString()

                try {
                    lifecycleScope.launch {
                        repository.updateReminder(reminder)
                    }

                    message("Recordatorio actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el recordatorio")
                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el recordatorio ${reminder.title}?")
                    .setPositiveButton("Aceptar"){ _,_ ->
                        try {
                            lifecycleScope.launch {
                                repository.deleteReminder(reminder)
                            }

                            message("Recordatorio eliminado exitosamente")

                            //Actualizar la UI
                            updateUI()

                        }catch(e: IOException){
                            e.printStackTrace()
                            message("Error al eliminar el recordatorio")
                        }
                    }
                    .setNegativeButton("Cancelar"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .create()
                    .show()


            })
        }


        return dialog
    }

    //Cuando se destruye el fragment
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //Se llama después de que se muestra el diálogo en pantalla
    override fun onStart() {
        super.onStart()

        val alertDialog =
            dialog as AlertDialog //Lo usamos para poder emplear el método getButton (no lo tiene el dialog)
        saveButton = alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        saveButton?.isEnabled = false

        binding.editTextNameReminder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })



        binding.SpinnerCategory.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                isSpinnerOpen = true
            }
            false
        }

        binding.SpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (isSpinnerOpen) {
                    saveButton?.isEnabled = validateFields()
                }
                isSpinnerOpen = false
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }




        binding.editTextMountReminder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })

        binding.editTextDateReminder.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }

        })
        binding.editTextHourReminder.addTextChangedListener (object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                saveButton?.isEnabled = validateFields()
            }
        })

    }

    private fun validateFields() =
        (binding.editTextNameReminder.text.toString().isNotEmpty() && binding.editTextMountReminder.text.toString()
            .isNotEmpty() && binding.editTextDateReminder.text.toString().isNotEmpty()  && isSpinnerOpen && binding.editTextHourReminder.text.toString().isNotEmpty() )
    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }

    private fun buildDialog(
        btn1Text: String,
        btn2Text: String,
        positiveButton: () -> Unit,
        negativeButton: () -> Unit
    ): Dialog =
        builder.setView(binding.root)
            .setTitle("Recordatorio")
            .setPositiveButton(btn1Text, DialogInterface.OnClickListener { dialog, which ->
                //Acción para el botón positivo
                positiveButton()
            })
            .setNegativeButton(btn2Text) { _, _ ->
                //Acción para el botón negativo
                negativeButton()
            }
            .create()


}
