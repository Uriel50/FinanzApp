package com.camu.finanzapp.perfil

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.databaseusers.data.DBRepository
import com.camu.finanzapp.databaseusers.data.db.DBDataBase
import com.camu.finanzapp.databinding.ActivityPerfilBinding
import com.camu.finanzapp.login.LoginActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import com.squareup.picasso.Picasso


class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        firebaseAuth = FirebaseAuth.getInstance()

        val singInAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        if (singInAccount!=null && firebaseAuth.currentUser!=null){
            Toast.makeText(this,"usuario autenticado en activity perfil", Toast.LENGTH_LONG).show()

            Picasso.get()
                .load(firebaseAuth.currentUser?.photoUrl)
                .into(binding.imagePerfilButton)

            binding.textName.text = firebaseAuth.currentUser?.displayName
            binding.textUser.text = "@"+firebaseAuth.currentUser?.displayName
            binding.textEmail.text = firebaseAuth.currentUser?.email
            binding.textSex.text = "?"

        }
        else{
            val database = DBDataBase.getDataBase(this)
            val repository = DBRepository(database.userDao())
            val userEmail = getUserEmail()

            lifecycleScope.launch {
                val user = repository.getUserByEmail(userEmail)
                val nickname = user?.nickname
                val name = user?.name
                val lastname = user?.lastname
                val sex = user?.sex

                binding.textName.text = name+" "+lastname
                binding.textUser.text = "@"+nickname
                binding.textEmail.text = userEmail
                binding.textSex.text = sex

            }
        }



        val backButton = binding.backButton

        backButton.setOnClickListener {
            onBackPressed()
        }

        val singOutButton = binding.textButtonSignOut

        singOutButton.setOnClickListener {
            firebaseAuth.signOut()

            //Desasociamos la app con la cuenta Google
            GoogleSignIn.getClient(this, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build())
                .signOut().addOnSuccessListener {
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }.addOnFailureListener{
                    Toast.makeText(this,"No se pudo cerrar sesion", Toast.LENGTH_LONG).show()
                }
        }




    }

    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(sharedPrefKey, "") ?: ""
    }
}