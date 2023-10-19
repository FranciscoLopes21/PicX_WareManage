package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.google.android.material.datepicker.MaterialDatePicker

class ListAtividadesActivity : AppCompatActivity() {

    private lateinit var atividadeViewModel: AtividadeViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var listAtividadesAdapter: ListAtividadesAdapter
    private lateinit var ibtn_calendar: ImageButton
    private lateinit var ibtn_backMain: ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_atividades)

        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)
        ibtn_calendar = findViewById(R.id.ibtn_calendar)
        ibtn_backMain = findViewById(R.id.ibtn_backMain)
        recyclerView = findViewById(R.id.rcl_listAtividades)

        recyclerView.layoutManager = LinearLayoutManager(this)

        listAtividadesAdapter = ListAtividadesAdapter(ArrayList())
        recyclerView.adapter = listAtividadesAdapter

        atividadeViewModel.atividades.observe(this) { atividades ->
            Log.d("WarehouseViewModel", "Warehouses updated: ${atividades.size}")
            // Atualizar diretamente a lista do adaptador
            listAtividadesAdapter.atividadeList.clear()
            listAtividadesAdapter.atividadeList.addAll(atividades)
            listAtividadesAdapter.notifyDataSetChanged()
        }

        atividadeViewModel.loadUserAtividades()

        ibtn_backMain.setOnClickListener {
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
        }

    }


}