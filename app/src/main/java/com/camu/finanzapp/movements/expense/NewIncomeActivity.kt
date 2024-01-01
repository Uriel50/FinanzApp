package com.camu.finanzapp.movements.expense

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.camu.finanzapp.R
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.IncomeEntity
import com.camu.finanzapp.databinding.ActivityNewIncomeBinding
import com.camu.finanzapp.movements.income.IncomeCategoryAdapter
import com.camu.finanzapp.movements.income.IncomeItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth

class NewIncomeActivity(
    private val newIncome: Boolean = true,
    private val income: IncomeEntity = IncomeEntity(
    incomeName = "",
    incomeCategory = "",
    incomeDate = "",
    incomeMount = 0.0,
    userEmailIncome = "",
    budgetId = 1
),
    private val updateUI: ()->Unit,
    private val message: (String) -> Unit
) : AppCompatActivity() {

    private lateinit var binding: ActivityNewIncomeBinding
    private lateinit var repository: DataBaseRepository
    private var spinnerItemSelected: String = "Salario"
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewIncomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = FirebaseAuth.getInstance()

        val singInAccount : GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)
        var SpinnerCategory: Spinner
        SpinnerCategory = binding.SpinnerCategory

        val categoryItems = listOf(
            IncomeItem("Salario"),
            IncomeItem("Inversiones"),
            IncomeItem("Alquileres"),
            IncomeItem("Pension"),
            IncomeItem("Regalias"),
            IncomeItem("Otros")
        )
        val adapter = IncomeCategoryAdapter(this,categoryItems)
        adapter.setDropDownViewResource(R.layout.spinner_item_movements)
        SpinnerCategory.adapter = adapter

        SpinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, id: Long) {
                spinnerItemSelected = categoryItems[position].text

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                spinnerItemSelected = "Salario"

            }
        }

        binding.apply {

            if (newIncome){
                editTextMountIncome.text = null
            }else{
                editTextMountIncome.setText(income.incomeMount.toString())
            }
            editTextNameIncome.setText(income.incomeName)


            val indexToSelect = categoryItems.indexOfFirst { it.text == income.incomeCategory }

            if (indexToSelect != -1){
                SpinnerCategory.setSelection(indexToSelect)
            }

        }


    }
}