package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.AtividadeListViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.example.warehouse.ViewModels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class AtividadeProduto : AppCompatActivity() {

    private lateinit var ibtn_backMain: ImageButton
    private lateinit var Spin_warehouseName: Spinner
    private lateinit var warehouseViewModel: WarehouseViewModel
    private lateinit var productViewModel: ProductViewModel
    private lateinit var atividadeViewModel: AtividadeViewModel
    private lateinit var atividadeListViewModel: AtividadeListViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var warehouseAdapter: ArrayAdapter<Warehouse>
    private lateinit var productAdapterAtividade: ProductAdapterAtividade
    private lateinit var txt_qunatProd: TextView
    private lateinit var btn_add_atividade: Button
    private lateinit var txtNomeAtividade: TextView
    var atividadeId = ""
    var nomeAtividade = ""
    var warehouseId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_atividade_produto)

        warehouseViewModel = ViewModelProvider(this)[WarehouseViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)
        atividadeListViewModel = ViewModelProvider(this).get(AtividadeListViewModel::class.java)

        ibtn_backMain = findViewById(R.id.ibtn_backMain)
        Spin_warehouseName = findViewById(R.id.Spin_warehouseName)
        recyclerView = findViewById(R.id.rcl_products)
        txt_qunatProd = findViewById(R.id.txt_qunatProd)
        btn_add_atividade = findViewById(R.id.btn_add_atividade)
        txtNomeAtividade = findViewById(R.id.txtNomeAtividade)

        atividadeId = intent.getStringExtra("atividadeId").toString()
        nomeAtividade = intent.getStringExtra("nomeAtividade").toString()

        productAdapterAtividade = ProductAdapterAtividade(ArrayList(), atividadeListViewModel, productViewModel, txt_qunatProd, atividadeId, nomeAtividade) { /* onItemClick logic here */ }
        warehouseAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, mutableListOf())
        warehouseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spin_warehouseName.adapter = warehouseAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = productAdapterAtividade

        atividadeViewModel.getWarehouseNameById(atividadeId,txtNomeAtividade)

        atividadeViewModel.atividadeId.observe(this, Observer { atividadeId ->
            initializeAdapter(atividadeId)
        })

        ibtn_backMain.setOnClickListener {
            showExitConfirmationDialog()
        }

        Spin_warehouseName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedWarehouse = warehouseAdapter.getItem(position)
                if (selectedWarehouse != null) {
                    warehouseId = selectedWarehouse.id
                    productViewModel.loadProductsByWarehouse(warehouseId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
            }
        }

        warehouseViewModel.warehouses.observe(this, Observer { warehouses ->
            warehouseAdapter.clear()
            warehouseAdapter.addAll(warehouses)
            warehouseAdapter.notifyDataSetChanged()
        })

        warehouseViewModel.loadUserWarehouses()

        Spin_warehouseName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val selectedWarehouseId = warehouseViewModel.warehouses.value?.get(position)?.id
                selectedWarehouseId?.let {
                    productViewModel.loadProductsByWarehouse(selectedWarehouseId)
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }

        productViewModel.products.observe(this) { products ->
            productAdapterAtividade.updateData(products)
        }

        btn_add_atividade.setOnClickListener {
            showDialog()
        }
    }

    private fun initializeAdapter(atividadeId: String) {
        productAdapterAtividade = ProductAdapterAtividade(ArrayList(), atividadeListViewModel, productViewModel, txt_qunatProd, atividadeId, nomeAtividade) { /* onItemClick logic here */ }
        recyclerView.adapter = productAdapterAtividade
    }

    private fun showExitConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
            .setMessage(R.string.dialogMessage)
            .setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { _, _ ->
            DeleteAtividade()
            val intent = Intent(this, DashBoardActivity::class.java)
            startActivity(intent)
        }

        builder.setNegativeButton("No") { _, _ -> }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun DeleteAtividade() {
        if (atividadeId != null) {
            atividadeViewModel.deleteWarehouse(atividadeId)
            atividadeListViewModel.deleteWarehouse(atividadeId)
        }
    }

    override fun onBackPressed() {
        showExitConfirmationDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        DeleteAtividade()
    }

    override fun onStop(){
        super.onStop()
        DeleteAtividade()
    }

    private fun showDialog(){
        val bottomDialog = BottomSheetDialog(this)
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_listaprodutos, null)
        var recyclerView: RecyclerView
        var productAtividadeListAdapter : ProductAtividadeListAdapter

        recyclerView = bottomView.findViewById(R.id.rcl_productslista)
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAtividadeListAdapter = ProductAtividadeListAdapter(ArrayList(), atividadeListViewModel)
        recyclerView.adapter = productAtividadeListAdapter

        atividadeListViewModel.atividades.observe(this){atividades ->
            productAtividadeListAdapter.productatividadeList.clear()
            productAtividadeListAdapter.productatividadeList.addAll(atividades)
            productAtividadeListAdapter.notifyDataSetChanged()
        }

        atividadeListViewModel.loadUserProducts(atividadeId)

        btnConfirm.setOnClickListener {
            bottomDialog.dismiss()
        }

        bottomDialog.setContentView(bottomView)
        bottomDialog.show()
    }
}