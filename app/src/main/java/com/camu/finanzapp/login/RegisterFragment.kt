package com.camu.finanzapp.login

import android.content.Context
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import androidx.fragment.app.Fragment
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.databaseusers.data.DBRepository
import com.camu.finanzapp.databaseusers.data.db.DBDataBase
import com.camu.finanzapp.databaseusers.data.db.model.DBEntity
import com.camu.finanzapp.databinding.FragmentRegisterBinding
import kotlinx.coroutines.launch
import org.mindrot.jbcrypt.BCrypt
import android.content.res.Resources
import com.camu.finanzapp.alerts.CustomDialogFragment

// Fragmento que representa la pantalla de registro
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private var spinnerItemSelected: String = "Masculino"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRegisterBinding.bind(view)

        var UserEditText: EditText
        var NameEditText: EditText
        var LastNameEditText: EditText
        var SpinnerSex: Spinner
        var emailEditText: EditText
        var passwordEditText: EditText
        val btRegister = binding.buttonRegister
        val textAlertEmail : TextView

        SpinnerSex = binding.spinnerSex
        UserEditText = binding.editTextUser
        NameEditText = binding.editTextName
        LastNameEditText = binding.editTextLastName
        emailEditText = binding.editTextEmail
        passwordEditText = binding.editTextPassword
        textAlertEmail = binding.textAlertEmail

        // Configura un adaptador para el Spinner de género
        val datos = arrayListOf("Masculino", "Femenino")
        val adaptador = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, datos)
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        SpinnerSex.adapter = adaptador

        SpinnerSex.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = datos[position]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Masculino"

            }
        }

        fun validateFieldsAndToggleButton() {
            val user = UserEditText.text.toString()
            val name = NameEditText.text.toString()
            val lastName = LastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val sex = spinnerItemSelected

            val isFieldsNotEmpty = user.isNotEmpty() && name.isNotEmpty() &&
                    lastName.isNotEmpty() && email.isNotEmpty() &&
                    password.isNotEmpty() && sex.isNotEmpty()

            Log.d("VALIDATE CAMPS","Validacion de campos : ${isFieldsNotEmpty}")

            val resources: Resources = resources

            val colorButtonEnabled = resources.getColor(R.color.buttoncolor)
            val colorButtonDisabled = resources.getColor(R.color.disable)

            btRegister.isEnabled = isFieldsNotEmpty // Habilita o deshabilita el botón según si los campos están llenos o no
            btRegister.setBackgroundColor(if (isFieldsNotEmpty) colorButtonEnabled else colorButtonDisabled)

        }

        UserEditText.addTextChangedListener { validateFieldsAndToggleButton() }
        NameEditText.addTextChangedListener { validateFieldsAndToggleButton() }
        LastNameEditText.addTextChangedListener { validateFieldsAndToggleButton() }
        emailEditText.addTextChangedListener { validateFieldsAndToggleButton() }
        passwordEditText.addTextChangedListener { validateFieldsAndToggleButton() }




        // Agrega un OnClickListener al botón de registro
        btRegister.setOnClickListener {
            val user = UserEditText.text.toString()
            val name = NameEditText.text.toString()
            val lastName = LastNameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val sex = spinnerItemSelected // Obtiene el género seleccionado





            // Valida el formato del correo electrónico utilizando una expresión regular
            if (!isValidEmail(email)) {
                // El correo electrónico no es válido, muestra un mensaje de error
                textAlertEmail.visibility = View.VISIBLE
                val cardEmail = binding.cardTextEmail
                val layoutParams = cardEmail.layoutParams as ConstraintLayout.LayoutParams

                layoutParams.topToBottom = textAlertEmail.id



                return@setOnClickListener
            }

            //**********************************************************************************

            // Valida si el correo electrónico ya existe en la base de datos
            val database = DBDataBase.getDataBase(requireContext())
            val repository = DBRepository(database.userDao())

            lifecycleScope.launch {
                val existingUser = repository.getUserByEmail(email)
                if (existingUser != null) {
                    // El usuario ya existe, muestra un mensaje de error
                    textAlertEmail.text = "El correo electrónico ya está en uso."
                    textAlertEmail.visibility = View.VISIBLE
                    val cardEmail = binding.cardTextEmail
                    val layoutParams = cardEmail.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.topToBottom = textAlertEmail.id
                } else {
                    // El usuario no existe, puedes continuar con el registro

                    // Genera una sal aleatoria
                    val salt = BCrypt.gensalt()

                    // Aplica hash a la contraseña con la sal
                    val hashedPassword = BCrypt.hashpw(password, salt)

                    // Crea un objeto DBEntity con la contraseña hasheada y la sal
                    val userEntity = DBEntity(
                        nickname = user,
                        name = name,
                        lastname = lastName,
                        sex = sex,
                        email = email,
                        key = hashedPassword // Almacena la contraseña hasheada en lugar de la contraseña en texto claro
                    )

                    // Luego, continuar con la inserción del usuario en la base de datos
                    repository.insertUser(userEntity)

                    // Crea un intento para pasar datos a la actividad de inicio (HomeActivity)

                    val correctRegisterDialog = CustomDialogFragment("Registro exitoso","Inicia sesion con tu correo y contraseña",true)
                    correctRegisterDialog.show(parentFragmentManager, "CorrectRegisterDialog")


                }
            }
        }

        val passTextView = binding.editTextPassword
        val showPasswordButton = binding.esconder
        val password = ""

        // Configura el campo de contraseña con asteriscos y un botón para mostrar/ocultar la contraseña
        passTextView.setText(password)
        passTextView.transformationMethod = PasswordTransformationMethod.getInstance()

        // Agrega un bloque setOnClickListener al botón showPasswordButton para mostrar/ocultar la contraseña
        showPasswordButton.setOnClickListener {
            if (passTextView.transformationMethod == null) {
                passTextView.transformationMethod = PasswordTransformationMethod.getInstance()
                showPasswordButton.setImageResource(R.mipmap.ic_ojocruzado_foreground)
            } else {
                passTextView.transformationMethod = null
                showPasswordButton.setImageResource(R.mipmap.ic_ojo_foreground)
            }
        }



        // Agregar el TouchListener para cerrar el teclado
        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                // Obtiene el EditText actualmente enfocado
                val currentFocusView = view.findFocus()

                // Verifica si la vista actualmente enfocada es un EditText
                if (currentFocusView is EditText) {
                    // Cierra el teclado virtual
                    val inputMethodManager =
                        requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.hideSoftInputFromWindow(
                        currentFocusView.windowToken,
                        0
                    )
                    currentFocusView.clearFocus()
                }
            }
            false
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
    // Función para validar el formato del correo electrónico utilizando una expresión regular
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$"
        return email.matches(emailRegex.toRegex())
    }





}
