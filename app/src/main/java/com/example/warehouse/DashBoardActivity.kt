package com.example.warehouse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ViewModels.WarehouseViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class DashBoardActivity : AppCompatActivity() {

    private lateinit var warehouseViewModel: WarehouseViewModel
    private lateinit var atividadeViewModel: AtividadeViewModel

    private lateinit var cardAtividadeCreate : CardView
    private lateinit var cardWarehousesData : CardView
    private lateinit var txtTotalWarehouses: TextView
    private lateinit var cardListAtividades : CardView
    private lateinit var ibtn_logout : ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        warehouseViewModel = ViewModelProvider(this).get(WarehouseViewModel::class.java)
        atividadeViewModel = ViewModelProvider(this).get(AtividadeViewModel::class.java)

        cardAtividadeCreate = findViewById(R.id.cardAtividadeCreate)
        cardWarehousesData = findViewById(R.id.cardWarehousesData)
        txtTotalWarehouses = findViewById(R.id.txtTotalWarehouses)
        cardListAtividades = findViewById(R.id.cardListAtividades)
        ibtn_logout = findViewById(R.id.ibtn_logout)

        val currentUser = FirebaseAuth.getInstance().currentUser
        var id = currentUser?.uid
        Toast.makeText(this, "$id", Toast.LENGTH_SHORT).show()

        //idFABAdd = findViewById(R.id.idFABAdd)
        warehouseViewModel.getNWarehouses(txtTotalWarehouses)

        cardAtividadeCreate.setOnClickListener {
            showDialogAtividade()
        }

        cardWarehousesData.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cardListAtividades.setOnClickListener {
            val intent = Intent(this, ListAtividadesActivity::class.java)
            startActivity(intent)
        }

        ibtn_logout.setOnClickListener {

        }

    }

    private fun showDialogAtividade(){

        // on below line we are creating a new bottom sheet dialog.
        val bottomDialog = BottomSheetDialog(this)

        // on below line we are inflating a layout file which we have created.
        val bottomView = layoutInflater.inflate(R.layout.bottomsheetdialog_atividade, null)

        // on below line we are creating a variable for our button
        // which we are using to dismiss our dialog.
        var btnConfirm = bottomView.findViewById<MaterialButton>(R.id.Btn_Confirm)
        var campoAtividade = bottomView.findViewById<TextInputEditText>(R.id.EdtAtividade)
        var layoutWarehouse = bottomView.findViewById<TextInputLayout>(R.id.txtInputActivityName)



        // on below line we are adding on click listener
        // for our dismissing the dialog button.
        btnConfirm.setOnClickListener {
            // on below line we are calling a dismiss
            // method to close our dialog.
            var nomeAtividade = campoAtividade.text.toString()
            if(nomeAtividade.isEmpty()){
                layoutWarehouse.error = "Campo vazio"
            }else {
                showCreateAtividadeDialog(nomeAtividade)
                bottomDialog.dismiss()
            }


        }
        // below line is use to set cancelable to avoid
        // closing of dialog box when clicking on the screen.

        // on below line we are setting
        // content view to our view.
        bottomDialog.setContentView(bottomView)

        // on below line we are calling
        // a show method to display a dialog.
        bottomDialog.show()

    }

    private fun showCreateAtividadeDialog(nomeAtividade: String) {

        val nameAt = nomeAtividade
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { userId ->
            atividadeViewModel.createAtividades(nameAt)
        }

        atividadeViewModel.atividadeId.observe(this, Observer { atividadeId ->
            val intent = Intent(this, AtividadeProduto::class.java)
            intent.putExtra("atividadeId", atividadeId)
            intent.putExtra("nomeAtividade", nomeAtividade)
            startActivity(intent)
        })

    }

}