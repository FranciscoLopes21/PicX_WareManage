package com.example.warehouse

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Models.Warehouse
import com.example.warehouse.Models.AtividadeProdutos
import com.example.warehouse.Models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductAtividadeListAdapter (val productatividadeList: ArrayList<AtividadeProdutos>) :
                        RecyclerView.Adapter<ProductAtividadeListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeProduct)
        val quant: TextView = itemView.findViewById(R.id.txtQuantProduct)
        val warehouse: TextView = itemView.findViewById(R.id.txtWareouse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_productatividadelist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productatividadeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productatividadeList[position]
        holder.name.text = productatividadeList[position].prodNome.toString()
        holder.quant.text = productatividadeList[position].quantProd.toString()
        holder.warehouse.text = productatividadeList[position].warehouseNome.toString()

    }

    fun getProductAt(position: Int): AtividadeProdutos {
        return productatividadeList[position]
    }

    fun deleteProductItem(position: Int) {
        val deleteProduct = productatividadeList.removeAt(position)
        notifyItemRemoved(position)

        Log.d("WarehouseAdapter", "Deleted warehouse ID: ${deleteProduct.idProd}")

    }

}