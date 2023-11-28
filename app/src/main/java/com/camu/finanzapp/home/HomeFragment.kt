package com.camu.finanzapp.home

import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.camu.finanzapp.R
import com.camu.finanzapp.database.BudgetEntity
import com.camu.finanzapp.database.DataBase
import com.camu.finanzapp.database.DataBaseRepository
import com.camu.finanzapp.database.ReminderEntity
import com.camu.finanzapp.database.TotalsEntity
import com.camu.finanzapp.databinding.FragmentHomeBinding
import com.camu.finanzapp.home.financedata.BalanceFragment
import com.camu.finanzapp.home.financedata.ChartGeneralFragment
import com.camu.finanzapp.home.carrousel.CarouselAdapter
import com.camu.finanzapp.home.carrousel.CarouselData
import com.camu.finanzapp.perfil.PerfilActivity
import com.camu.finanzapp.reminders.ReminderAdapter
import com.camu.finanzapp.util.Constants.DATABASE_NAME_TABLE_REMINDER
import com.camu.finanzapp.util.GlobalData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import java.util.Locale


// Fragmento de la pantalla de inicio
class HomeFragment : Fragment(R.layout.fragment_home) {



    private lateinit var binding: FragmentHomeBinding
    private var remindersDates : List<String> = emptyList()
    private lateinit var database: DataBase
    private lateinit var repository: DataBaseRepository
    private lateinit var reminderAdapter: ReminderAdapter
    private var reminders: List<ReminderEntity> = emptyList()
    private var isStrategy: BudgetEntity?=null
    private var isChartGeneral: TotalsEntity? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincula la vista del fragmento con la clase FragmentHomeBinding
        binding = FragmentHomeBinding.bind(view)
        database = DataBase.getDataBase(requireContext())
        repository = DataBaseRepository(
            database.userDao(),
            database.totalDao(),
            database.reminderDao(),
            database.incomeDao(),
            database.expenseDao(),
            database.budgetDao()
        )
        reminderAdapter = ReminderAdapter(){}



        //********************Carrusel********************
        // Obtiene una referencia al ViewPager2 desde la vista
        val viewPager: ViewPager2 = binding.viewPager

        // Lista de datos para el carrusel
        val carouselDataList = listOf(
            CarouselData("Ahorra. Procura que sea una cantidad fija y no sólo lo que te sobre en la semana, un buen ahorro es la base para lograr una inversión que te permitirá multiplicar tu dinero a futuro."),
            CarouselData("Invierte tu dinero. Al hacerlo podrás generar un capital extra, siempre busca la opción que más te convenga, una buena opción es invertir en los Certificados de la Tesorería de la Federación, mejor conocidos como CETES."),
            CarouselData("Usa el crédito de manera responsable. Hazlo tu mejor aliado, recuerda que el crédito no es un dinero extra, es una cantidad que te presta el banco por la cual estás obligado a pagarla, de lo contrario te generará muchos intereses y un mal historial crediticio.")
        )


        viewPager.adapter = CarouselAdapter(carouselDataList)
        viewPager.offscreenPageLimit = 3

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.viewpager_current_item_margin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.viewpager_next_item_visible)
        viewPager.setPageTransformer { page, position ->
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }

        // Establece la posición inicial para el efecto infinito
        val initialPosition = 2
        viewPager.setCurrentItem(initialPosition, false)



//        // Crea un adaptador para el carrusel y establece los datos
//        val carouselAdapter = CarouselAdapter(carouselDataList)
//        viewPager.adapter = carouselAdapter
//        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL


        //*******************Balance***********************


        val fragmentManager = childFragmentManager
        val transaction = fragmentManager.beginTransaction()
        val containerBalanceGeneral = binding.frameBalanceGeneral

        lifecycleScope.launch {
            val email = getCurrentUserEmail()
            isStrategy = repository.getBudgetByEmail(email)
            if (isStrategy!=null){
                val balanceFragment = BalanceFragment()

                transaction.replace(containerBalanceGeneral.id,balanceFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            else{

                val newStrategy = NewStrategyFragment()
                transaction.replace(containerBalanceGeneral.id,newStrategy)
                transaction.addToBackStack(null)
                transaction.commit()

            }

        }



        //**********Graficas*******************************

        lifecycleScope.launch{

            val containerChartGeneral = binding.frameChartGeneral
            val email = getCurrentUserEmail()
            isChartGeneral = repository.getTotalByEmail(email)


            if (isChartGeneral?.totalIncome != 0.0 && isChartGeneral?.totalExpense!=0.0 && isChartGeneral!=null){

                val chartFragment = ChartGeneralFragment()
                val fragmentManager = childFragmentManager

                val transaction = fragmentManager.beginTransaction()
                transaction.replace(containerChartGeneral.id, chartFragment)
                transaction.addToBackStack(null)
                transaction.commit()
            }
            else{
                val blancChartGeneral = R.layout.empy_chart_general

                val inflater = LayoutInflater.from(requireContext())
                val chartView = inflater.inflate(blancChartGeneral, containerChartGeneral, false)
                containerChartGeneral.addView(chartView)
            }

        }




        //*******Recordatorios*************

        binding.listReminders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = reminderAdapter
        }

        upDateUiReminder()





        //***********************
        //perfil
        //***********************

        val perfilButton = binding.perfilButton
        perfilButton.setOnClickListener {
            val intent = Intent(context, PerfilActivity::class.java)
            startActivity(intent)
        }





    }

    private fun upDateUiReminder(){
        lifecycleScope.launch {
            val userEmail = getCurrentUserEmail()
            reminders = repository.getAllReminders().filter { it.userEmailReminder == userEmail }


            if(reminders.isNotEmpty()){
                // Hay por lo menos un registro
                binding.tvSinRegistros.visibility = View.INVISIBLE
                binding.blankReminderIcon.visibility = View.INVISIBLE
                binding.textSoonReminder.visibility = View.VISIBLE


            } else {
                // No hay registros
                binding.tvSinRegistros.visibility = View.VISIBLE
                binding.blankReminderIcon.visibility = View.VISIBLE
                binding.textSoonReminder.visibility = View.INVISIBLE
            }
            //aqui se usa get nearestDate
            reminderAdapter.updateList(getNearestDates(reminders))

        }
    }




    fun getNearestDates(reminders: List<ReminderEntity>): List<ReminderEntity> {
        val today = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val nearestEntities = mutableListOf<ReminderEntity>()

        // Inicializa con un valor muy grande para garantizar que cualquier fecha real sea menor
        var smallestDiff = Long.MAX_VALUE

        for (reminder in reminders) {
            dateFormat.parse(reminder.reminderDate)?.let {
                val date = Calendar.getInstance().apply { time = it }
                if (date.before(today)) {
                    date.set(Calendar.YEAR, today.get(Calendar.YEAR))
                }
                val diff = Math.abs(date.timeInMillis - today.timeInMillis)

                if (diff < smallestDiff) {
                    smallestDiff = diff
                    nearestEntities.clear()
                    nearestEntities.add(reminder)
                } else if (diff == smallestDiff) {
                    nearestEntities.add(reminder)
                } else {

                }
            }
        }

        return nearestEntities
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
        fun newInstance() = HomeFragment()
    }
}
