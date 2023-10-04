package com.example.warehouse

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse.Models.Product
import com.example.warehouse.ViewModels.AtividadeListViewModel
import com.example.warehouse.ViewModels.ProductViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class ProductAdapterAtividade(
    val productList: ArrayList<Product>,
    private val atividadeListViewModel: AtividadeListViewModel,
    private val productViewModel: ProductViewModel,
    private val txtQuantProd: TextView,
    private var atividadeId: String?,
    private var nomeAtividade: String?,
    private val onItemClick: () -> Unit
) : RecyclerView.Adapter<ProductAdapterAtividade.ViewHolder>() {



    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.txtNomeProduct)
        val quant: TextView = itemView.findViewById(R.id.txtQuantProduct)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_productatividade, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun setAtividadeId(id: String) {
        atividadeId = id
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = productList[position]
        holder.name.text = productList[position].nomeProd.toString()
        holder.quant.text = productList[position].quantProd.toString()


        val quantidade = productList[position].quantProd

        if (quantidade > 0) {

            holder.itemView.setOnClickListener {
                val context = holder.itemView.context
                val bottomSheetDialog = BottomSheetDialog(context)
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.bottomsheetdialog_productatividade, null)

                val txtNewProduct = view.findViewById<TextView>(R.id.txtNewProduct)
                val btnDecrement = view.findViewById<Button>(R.id.btnDecrement)
                val numberPicker = view.findViewById<NumberPicker>(R.id.numberPicker)
                val btnIncrement = view.findViewById<Button>(R.id.btnIncrement)
                val Btn_Confirm = view.findViewById<MaterialButton>(R.id.Btn_Confirm)

                txtNewProduct.text = product.nomeProd
                numberPicker.minValue = 0
                numberPicker.maxValue = product.quantProd

                btnIncrement.setOnClickListener { numberPicker.value = numberPicker.value + 1 }
                btnDecrement.setOnClickListener { numberPicker.value = numberPicker.value - 1 }

                Btn_Confirm.setOnClickListener {
                    val quantidadeAdicionada = numberPicker.value
                    val quantStock = product.quantProd - quantidadeAdicionada
                    // Chame o método da ViewModel para criar a atividade

                    atividadeId?.let { it1 ->
                        nomeAtividade?.let { it2 ->
                            atividadeListViewModel.createAtividades(
                                it1, it2,
                                product.idProduct, product.nomeProd, quantidadeAdicionada,
                                product.warehouseId
                            )
                            Log.d("IdWagdfgdfgdfgdfgdreouse", "$product.warehouseId")
                        }
                        atividadeListViewModel.getNWarehouses(it1, txtQuantProd)
                        productViewModel.updateQuantProduct(product.idProduct, quantStock)

                    }



                    bottomSheetDialog.dismiss() // Feche o BottomSheetDialog após criar a atividade
                }

                bottomSheetDialog.setContentView(view)
                bottomSheetDialog.show()
            }
        }
    }

    fun updateData(newProductList: List<Product>) {
        productList.clear()
        productList.addAll(newProductList)
        notifyDataSetChanged()
    }
}