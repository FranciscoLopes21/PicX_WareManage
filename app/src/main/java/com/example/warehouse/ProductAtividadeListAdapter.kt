package com.example.warehouse

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.warehouse.Models.AtividadeProdutos
import com.example.warehouse.Models.Product
import com.example.warehouse.ViewModels.AtividadeListViewModel
import com.example.warehouse.ViewModels.AtividadeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ProductAtividadeListAdapter(
    val productatividadeList: ArrayList<AtividadeProdutos>,
    private val atividadeListViewModel: AtividadeListViewModel,) : RecyclerView.Adapter<ProductAtividadeListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeProduct)
        val quant: TextView = itemView.findViewById(R.id.txtQuantProduct)
        val warehouse: TextView = itemView.findViewById(R.id.txtWareouse)
        val lottieDelete: LottieAnimationView = itemView.findViewById(R.id.animationView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_productatividadelist, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productatividadeList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productatividadeList[position]
        holder.name.text = productatividadeList[position].prodNome.toString()
        holder.quant.text = productatividadeList[position].quantProd.toString()
        holder.warehouse.text = productatividadeList[position].warehouseNome.toString()
        holder.lottieDelete.setOnClickListener {
            holder.lottieDelete.playAnimation()
            atividadeListViewModel.deleteActivityByProductId(product.idAtividade, product.idProd)

        }

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