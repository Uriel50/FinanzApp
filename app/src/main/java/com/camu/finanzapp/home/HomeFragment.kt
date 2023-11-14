package com.camu.finanzapp.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.camu.finanzapp.R
import com.camu.finanzapp.databinding.FragmentHomeBinding
import com.camu.finanzapp.home.financedata.BalanceFragment
import com.camu.finanzapp.home.financedata.ChartGeneralFragment
import com.camu.finanzapp.home.carrousel.CarouselAdapter
import com.camu.finanzapp.home.carrousel.CarouselData
import com.camu.finanzapp.perfil.PerfilActivity
import com.camu.finanzapp.util.GlobalData

// Fragmento de la pantalla de inicio
class HomeFragment : Fragment(R.layout.fragment_home) {



    private lateinit var binding: FragmentHomeBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Vincula la vista del fragmento con la clase FragmentHomeBinding
        binding = FragmentHomeBinding.bind(view)


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
        val isBalance = GlobalData.isBalance

        if (isBalance){
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


        //**********Graficas*******************************

        val containerChartGeneral = binding.frameChartGeneral
        val isChartGeneral = GlobalData.isChartGeneral

        if (isChartGeneral){

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








        //***********************
        //perfil
        //***********************

        val perfilButton = binding.perfilButton
        perfilButton.setOnClickListener {
            val intent = Intent(context, PerfilActivity::class.java)
            startActivity(intent)
        }





    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}
