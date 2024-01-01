package com.camu.finanzapp.movements

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.camu.finanzapp.database.*
import kotlinx.coroutines.launch
import java.util.Locale.Category

class FinanzappViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as DataBaseApplication
    private val repository: DataBaseRepository = app.repository
    private val userEmail = getUserEmail()

    val TotalByEmailLiveData: LiveData<TotalsEntity> by lazy {

        repository.getTotalByEmailLiveData(userEmail)
    }


    val BudgetByEmailLiveData: LiveData<BudgetEntity> by lazy {

        repository.getBudgetByEmailLiveData(userEmail)
    }

    //******Income operations **********************

    val AllIncomes : LiveData<List<IncomeEntity>> by lazy {
        repository.getAllIncomeLiveData(userEmail)
    }

    val AllExpense: LiveData<List<ExpenseEntity>> by lazy {
        repository.getAllExpenseLiveData(userEmail)
    }






    private fun getUserEmail(): String {
        val sharedPrefKey = "user_email"

        // Obtener el contexto de la Activity asociada
        val sharedPreferences = app?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Utilizar el contexto para acceder a SharedPreferences, si está disponible
        return sharedPreferences?.getString(sharedPrefKey, "") ?: ""
    }

}
