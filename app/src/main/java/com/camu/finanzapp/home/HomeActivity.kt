package com.camu.finanzapp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.camu.finanzapp.R
import com.camu.finanzapp.login.Data
import com.camu.finanzapp.login.DataAdapter
import com.google.android.material.tabs.TabLayout

// Actividad principal que representa la pantalla de inicio
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        // Obtiene una referencia al ViewPager y al TabLayout
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        // Crea un adaptador para el ViewPager y configura los íconos de las pestañas
        val adapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


        // Obtiene una referencia al fragmento de inicio y establece los argumentos
        val fragment = adapter.getItem(0)
        fragment?.arguments = intent.extras

        // Configura los íconos de las pestañas
        tabLayout.getTabAt(0)?.setIcon(R.mipmap.ic_home_foreground) // Icono para la primera pestaña
        tabLayout.getTabAt(1)?.setIcon(R.mipmap.ic_graphic_foreground) // Icono para la segunda pestaña
        tabLayout.getTabAt(2)?.setIcon(R.mipmap.ic_budget_foreground) // Icono para la tercera pestaña
        tabLayout.getTabAt(3)?.setIcon(R.mipmap.ic_reminder_foreground) // Icono para la cuarta pestaña
    }
}
