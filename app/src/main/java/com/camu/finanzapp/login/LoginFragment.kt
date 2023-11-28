package com.camu.finanzapp.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.R
import com.camu.finanzapp.alerts.CustomDialogFragment
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.UserEntity
import com.camu.finanzapp.databinding.FragmentLoginBinding
import com.camu.finanzapp.home.HomeActivity
import org.mindrot.jbcrypt.BCrypt
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Fragmento que representa la pantalla de inicio de sesión
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding

    //*************Google*************

    private var _binding: FragmentLoginBinding? = null
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }



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

            database = DataBase.getDataBase(requireContext())

            repository = DataBaseRepository(
                database.userDao(),
                database.totalDao(),
                database.reminderDao(),
                database.incomeDao(),
                database.expenseDao(),
                database.budgetDao()
            )

            // Busca el usuario por correo electrónico en la base de datos

            lifecycleScope.launch {
                val user = repository.getUserByEmail(email)
                if (user != null && BCrypt.checkpw(password, user.userKey)) {
                    // La contraseña es válida, el usuario puede iniciar sesión
                    // Puedes realizar alguna acción, como navegar a la actividad de inicio
                    val intent = Intent(context, HomeActivity::class.java)
                    saveUserEmail(email)
                    Log.d("Correo y contraseña","${user.userEmail}  ${BCrypt.checkpw(password, user.userKey)}")
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



        firebaseAuth = FirebaseAuth.getInstance()
        configureGoogleSignIn()

        binding.cardSignGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            resultLauncher.launch(signInIntent)
        }





    }

    private fun saveUserEmail(email: String) {
        val sharedPrefKey = "user_email"
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(sharedPrefKey, email)
        editor.apply()
    }




    private fun configureGoogleSignIn() {
        // Aquí usas getString(R.string.default_web_client_id) para obtener el ID de cliente
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("655666775423-gfkkl7q6tu84j626knt9e7f0vd68o058.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInAccount = GoogleSignIn.getLastSignedInAccount(requireContext())
        if (signInAccount != null && firebaseAuth.currentUser != null) {
            Toast.makeText(context, "Usuario autenticado", Toast.LENGTH_LONG).show()
            navigateToMain()
            saveUserEmail(firebaseAuth.currentUser?.email.toString())
        }
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            val signInAccount = task.getResult(ApiException::class.java)
            val authCredential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
            firebaseAuth.signInWithCredential(authCredential)
                .addOnSuccessListener {
                    Toast.makeText(context, "Tu cuenta se ha conectado con la aplicación", Toast.LENGTH_LONG).show()
                    navigateToMain()
                    saveUserEmail(firebaseAuth.currentUser?.email.toString())
                    database = DataBase.getDataBase(requireContext())

                    repository = DataBaseRepository(
                        database.userDao(),
                        database.totalDao(),
                        database.reminderDao(),
                        database.incomeDao(),
                        database.expenseDao(),
                        database.budgetDao()
                    )

                    val userFirebase= UserEntity(
                        userName = firebaseAuth.currentUser?.displayName.toString(),
                        userEmail = firebaseAuth.currentUser?.email.toString(),
                        userSex = "?",
                        userKey = "****",
                        userNickname = firebaseAuth.currentUser?.displayName.toString(),
                        userLastname = firebaseAuth.currentUser?.displayName.toString()
                    )

                    CoroutineScope(Dispatchers.IO).launch {

                        val existingUser = repository.getUserByEmail(firebaseAuth.currentUser?.email.toString())
                        if (existingUser == null) {
                            // El usuario no existe
                            repository.insertUser(userFirebase)
                        } else {
                            // El usuario ya existe
                        }
                    }


                }
                .addOnFailureListener {
                    Toast.makeText(context, "No se pudo completar el registro", Toast.LENGTH_LONG).show()
                }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }

    private fun navigateToMain() {
        // Implementa la navegación hacia la actividad principal o la pantalla que sigue después del inicio de sesión.

        val intent = Intent(context, HomeActivity::class.java)


        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}
