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
import com.example.warehouse.Models.Product
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductAdapter (val productList: ArrayList<Product>,
                      private val onItemClick: (Product) -> Unit) :
                        RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeProduct)
        val quant: TextView = itemView.findViewById(R.id.txtQuantProduct)
        val lnStock: LinearLayout = itemView.findViewById(R.id.ln_Stock)
        val txtRefProd: TextView = itemView.findViewById(R.id.txtRefProd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.name.text = productList[position].nomeProd.toString()
        holder.quant.text = productList[position].quantProd.toString()
        holder.txtRefProd.text = productList[position].referenciaProd.toString()

        val quantProd = productList[position].quantProd.toInt()
        val quantMinProd = productList[position].quantMinProd.toInt()

        if (quantProd == quantMinProd || quantProd < quantMinProd) {
            holder.lnStock.setBackgroundColor(Color.RED)
        } else {
            holder.lnStock.setBackgroundColor(Color.TRANSPARENT)
        }

        holder.itemView.setOnClickListener {
            onItemClick(product)
        }
    }

    fun getProductAt(position: Int): Product {
        return productList[position]
    }

    fun deleteProductItem(position: Int) {
        val deleteProduct = productList.removeAt(position)
        notifyItemRemoved(position)

        Log.d("WarehouseAdapter", "Deleted warehouse ID: ${deleteProduct.idProduct}")

    }

}