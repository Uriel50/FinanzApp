package com.camu.finanzapp.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.alerts.CustomDialogFragment
import com.camu.finanzapp.databinding.FragmentLoginBinding
import com.camu.finanzapp.home.HomeActivity
import org.mindrot.jbcrypt.BCrypt
import com.camu.finanzapp.database.data.DBRepository
import com.camu.finanzapp.database.data.db.DBDataBase
import kotlinx.coroutines.launch


// Fragmento que representa la pantalla de inicio de sesión
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

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

        val btRegister = binding.buttonRegister

        // Agrega un OnClickListener al botón para navegar a la pantalla de registro
        btRegister.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerViewFinal, RegisterFragment.newInstance())
                .addToBackStack("RegisterFragment")
                .commit()
        }



        val btSignIn = binding.buttonSingIn
        var emailEditText: EditText
        var passwordEditText: EditText

        emailEditText = binding.editTextName
        passwordEditText = binding.editTextPassword

        // Agrega un OnClickListener al botón de inicio de sesión
        btSignIn.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            val database = DBDataBase.getDataBase(requireContext())

            val repository = DBRepository(database.userDao())

            // Busca el usuario por correo electrónico en la base de datos

            lifecycleScope.launch {
                val user = repository.getUserByEmail(email)
                if (user != null && BCrypt.checkpw(password, user.key)) {
                    // La contraseña es válida, el usuario puede iniciar sesión
                    // Puedes realizar alguna acción, como navegar a la actividad de inicio
                    val intent = Intent(context, HomeActivity::class.java)
                    saveUserEmail(email)
                    Log.d("Correo y contraseña","${user.email}  ${BCrypt.checkpw(password, user.key)}")
                    startActivity(intent)
                }
                else{
                    val incorrectCredentialsDialog = CustomDialogFragment("Credenciales invalidas","Usuario o contraseña icorrectas",false)
                    incorrectCredentialsDialog.show(childFragmentManager, "IncorrectCredentialsDialog")
                }
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

    private fun saveUserEmail(email: String) {
        val sharedPrefKey = "user_email"
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPrefKey, email)
        editor.apply()
    }
    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
