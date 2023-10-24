package com.camu.finanzapp.perfil

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.camu.finanzapp.database.data.DBRepository
import com.camu.finanzapp.database.data.db.DBDataBase
import com.camu.finanzapp.database.data.db.model.DBEntity
import com.camu.finanzapp.databinding.ActivityPerfilBinding
import kotlinx.coroutines.launch


class PerfilActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPerfilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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

        val backButton = binding.backButton

        backButton.setOnClickListener {
            onBackPressed()
        }




    }

    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString(sharedPrefKey, "") ?: ""
    }
}