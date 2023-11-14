package com.camu.finanzapp.reminders

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.camu.finanzapp.R
import com.camu.finanzapp.databasereminders.application.RemindersDBApp
import com.camu.finanzapp.databasereminders.data.ReminderRepository
import com.camu.finanzapp.databasereminders.data.db.ReminderDatabase
import com.camu.finanzapp.databasereminders.data.db.model.ReminderEntity
import com.camu.finanzapp.databaseusers.data.DBRepository
import com.camu.finanzapp.databaseusers.data.db.DBDataBase
import com.camu.finanzapp.databinding.FragmentRemidersListBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class RemidersListFragment : Fragment(R.layout.fragment_remiders_list) {

    private lateinit var binding: FragmentRemidersListBinding

    private var reminders : List<ReminderEntity> = emptyList()
    private lateinit var database: ReminderDatabase
    private lateinit var repository: ReminderRepository


    private lateinit var reminderAdapter: ReminderAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentRemidersListBinding.bind(view)


        database = ReminderDatabase.getDatabase(requireContext())
        repository = ReminderRepository(database.ReminderDao())
        reminderAdapter = ReminderAdapter(){ reminder ->
            gameClicked(reminder)
        }

        //binding.rvGames.layoutManager = LinearLayoutManager(this@MainActivity)
        //binding.rvGames.adapter = gameAdapter

        binding.listReminders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderAdapter
        }

        updateUI()

        binding.buttonNewReminder.setOnClickListener {
            val dialog = ReminderDialog( updateUI = {
                updateUI()
            }, message = { text ->
                message(text)
            })
            dialog.show(parentFragmentManager, "dialog")
            Toast.makeText(requireContext(),"usuario autenticado en activity perfil", Toast.LENGTH_LONG).show()
        }

    }
    
    private fun gameClicked(game: ReminderEntity){
        //Toast.makeText(this, "Click en el juego con id: ${game.id}", Toast.LENGTH_SHORT).show()
        val dialog = ReminderDialog(newReminder = false, reminder = game, updateUI = {
            updateUI()
        }, message = { text ->
            message(text)
        })
        dialog.show(childFragmentManager, "dialog")
    }
    private fun updateUI(){
        lifecycleScope.launch {
            val userEmail = getCurrentUserEmail()
            reminders = repository.getAllReminders().filter { it.userReminderId == userEmail }

            if(reminders.isNotEmpty()){
                // Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
                binding.blankReminderIcon.visibility = View.INVISIBLE
            } else {
                // No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
                binding.blankReminderIcon.visibility = View.VISIBLE
            }
            reminderAdapter.updateList(reminders)
        }
    }


    private fun message(text: String){
        Snackbar.make(binding.cl, text, Snackbar.LENGTH_SHORT)
            .setTextColor(Color.parseColor("#FFFFFF"))
            .setBackgroundTint(Color.parseColor("#9E1734"))
            .show()
    }
    private fun getCurrentUserEmail(): String {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireContext())

        return when {
            firebaseUser != null -> firebaseUser.email ?: ""
            googleSignInAccount != null -> googleSignInAccount.email ?: ""
            else -> {
                val sharedPreferences = activity?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences?.getString("user_email", "") ?: ""
            }
        }
    }
    companion object {

        @JvmStatic
        fun newInstance() = RemidersListFragment()
    }
}