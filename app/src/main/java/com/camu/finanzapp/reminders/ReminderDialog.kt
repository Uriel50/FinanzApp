package com.camu.finanzapp.reminders


import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseApplication
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ReminderEntity
import com.camu.finanzapp.databinding.ReminderDialogBinding
import com.camu.finanzapp.util.ReminderItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class ReminderDialog (
    private val newReminder: Boolean = true,
    private val reminder: ReminderEntity = ReminderEntity(
        reminderTitle = "",
        reminderCategory = "",
        reminderDate = "",
        hour = "",
        reminderMount = 0.0,
        userEmailReminder = "",
        userId = 1
    ),
    private val updateUI: ()-> Unit,
    private val message: (String) -> Unit
): DialogFragment(){

    private var _binding: ReminderDialogBinding? = null
    private val binding get() = _binding!!

    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: Dialog

    private var saveButton: Button? = null

    private lateinit var repository: DataBaseRepository

    private var spinnerItemSelected: String = "Renta/Hipoteca"

    var isSpinnerOpen = false

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var selectedDate: String

    private lateinit var selectedTime:String

    private  var isSelectedDate:Boolean= false
    private  var isSelectedTime:Boolean= false

    //Se configura el diálogo inicial
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ReminderDialogBinding.inflate(requireActivity().layoutInflater)

        val app = requireActivity().application as DataBaseApplication
        repository = app.repository

        builder = AlertDialog.Builder(requireContext())


        firebaseAuth = FirebaseAuth.getInstance()


        //////////////////////////////////////////////////////////////////////////////////

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
            editTextNameReminder.setText(reminder.reminderTitle)
            editTextMountReminder.setText(reminder.reminderMount.toString())
            editTextDateReminder.setText(reminder.reminderDate)
            editTextHourReminder.setText(reminder.hour)

            val indexToSelect = categoryItems.indexOfFirst { it.text == reminder.reminderCategory }

            // Seleccion del elemento por su índice
            if (indexToSelect != -1) {
                SpinnerCategory.setSelection(indexToSelect)
            }

        }



        dialog = if (newReminder) {
            buildDialog("Guardar", "Cancelar", {
                //Create (Guardar)
                val category = spinnerItemSelected
                reminder.reminderTitle = binding.editTextNameReminder.text.toString()
                reminder.reminderCategory = category
                reminder.reminderMount = binding.editTextMountReminder.text.toString().toDouble()


                if (isSelectedDate){
                    reminder.reminderDate = selectedDate
                }
                if (isSelectedTime){
                    reminder.hour = selectedTime
                }




                try {
                    lifecycleScope.launch {
                        val userEmail = getUserEmail()
                        val userId = repository.getUserIdByEmail(userEmail)
                        if (userId != null){
                            reminder.userId = userId
                        }

                        repository.insertReminder(reminder)
                        withContext(Dispatchers.Main) {
                            updateUI()
                        }

                    }
                    if (singInAccount!=null && firebaseAuth.currentUser!=null){
                        reminder.userEmailReminder = firebaseAuth.currentUser?.email.toString()

                    }else{
                        val userEmail = getUserEmail()

                        lifecycleScope.launch {
                            val user = repository.getUserByEmail(userEmail)
                            reminder.userEmailReminder = user?.userEmail.toString()
                        }
                    }



                    message("Recordatorio Guardado")

                    isSelectedDate = false
                    isSelectedTime = false
                    updateUI()



                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al guardar el recordatorio")
                    isSelectedDate = false
                    isSelectedTime = false
                }
            }, {
                //Cancelar
            })
        } else {
            buildDialog("Actualizar", "Borrar", {
                //Update
                val category = spinnerItemSelected
                reminder.reminderTitle = binding.editTextNameReminder.text.toString()
                reminder.reminderCategory = category
                reminder.reminderMount = binding.editTextMountReminder.text.toString().toDouble()
                if (isSelectedDate){
                    reminder.reminderDate = selectedDate
                }
                if (isSelectedTime){
                    reminder.hour = selectedTime
                }

                try {
                    lifecycleScope.launch {
                        repository.updateReminder(reminder)
                    }

                    message("Recordatorio actualizado exitosamente")

                    //Actualizar la UI
                    updateUI()
                    isSelectedDate = false
                    isSelectedTime = false

                }catch(e: IOException){
                    e.printStackTrace()
                    message("Error al actualizar el recordatorio")
                    isSelectedDate = false
                    isSelectedTime = false
                }

            }, {
                //Delete

                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmación")
                    .setMessage("¿Realmente deseas eliminar el recordatorio ${reminder.reminderTitle}?")
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

        var NameEditText: EditText
        var MountEditText: EditText
        var DateEditText: EditText
        var SpinnerCategory: Spinner
        var TimeEditText: EditText

        NameEditText = binding.editTextNameReminder
        MountEditText = binding.editTextMountReminder
        DateEditText =binding.editTextDateReminder
        TimeEditText = binding.editTextHourReminder


        binding.editTextDateReminder.setOnClickListener {
            showDatePickerDialog()

        }


        binding.editTextHourReminder.setOnClickListener {
            showTimePickerDialog()
        }
        fun validateFields() {
            val Name = NameEditText.text.toString()
            val Mount = MountEditText.text.toString()
            val Date = DateEditText.text.toString()
            val Spinner = spinnerItemSelected
            val Time = TimeEditText.text.toString()
            val isFieldsNotEmpty = Name.isNotEmpty() && Mount.isNotEmpty() && Date.isNotEmpty() && Time.isNotEmpty() &&Spinner.isNotEmpty()


            saveButton?.isEnabled = isFieldsNotEmpty
        }

        NameEditText.addTextChangedListener { validateFields() }
        MountEditText.addTextChangedListener { validateFields() }
        DateEditText.addTextChangedListener { validateFields() }
        TimeEditText.addTextChangedListener { validateFields() }


    }


    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(requireContext(), { _, year, monthOfYear, dayOfMonth ->
            // Usa 'year', 'monthOfYear', y 'dayOfMonth' para tu lógica
            selectedDate = String.format("%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year)
            binding.editTextDateReminder.setText(selectedDate)
            isSelectedDate = true
        }, year, month, day)

        dpd.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val tpd = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            // Usa 'selectedHour' y 'selectedMinute' para tu lógica
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            binding.editTextHourReminder.setText(selectedTime)
            isSelectedTime = true
        }, hour, minute, false)

        tpd.show()
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
