package com.example.cafe2.adapter.admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemProductAdminBinding
import com.example.cafe2.databinding.ItemProductBinding
import com.example.cafe2.model.user.Product
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AddProductActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class ItemProductAdapter(val listProduct: MutableList<Product>) :
    RecyclerView.Adapter<ItemProductAdapter.ViewHolder>() {

    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    class ViewHolder(val itemProductBinding: ItemProductAdminBinding) :
        RecyclerView.ViewHolder(itemProductBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ItemProductAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Product = listProduct[position]
        val context = holder.itemView.context
        if (item.getImage().contains("https")) {
            Glide.with(context)
                .load(item.getImage())
                .placeholder(R.drawable.noanh)
                .error(R.drawable.error)
                .into(holder.itemProductBinding.imgProductAdmin)
        } else {
            val img = Utils.BASE_URL + "images/" + item.getImage()
            Glide.with(context)
                .load(img)
                .placeholder(R.drawable.noanh)
                .error(R.drawable.error)
                .into(holder.itemProductBinding.imgProductAdmin)
        }
        holder.itemProductBinding.imgProductAdmin
        holder.itemProductBinding.txtNameProductAdmin.text = item.getName()
        holder.itemProductBinding.txtPriceProductAdmin.text = item.getPrice().toString()
        holder.itemProductBinding.txtCategoryProductAdmin.text = item.getIdCategory().toString()
        holder.itemProductBinding.txtOutstandingAdmin
        holder.itemProductBinding.imgEditProduct.setOnClickListener{
            val  intent = Intent(context, AddProductActivity::class.java)
            intent.putExtra("editProduct", item)
            context.startActivity(intent)
        }
        holder.itemProductBinding.imgDeleteProduct.setOnClickListener {
            var builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Thông báo")
                .setIcon(R.drawable.baseline_announcement_24)
                .setMessage("Bạn có muốn xóa loại sản phẩm không")
                .setPositiveButton("Đồng ý") { dialog, which ->
                    deleteProduct(item.getId(), position, context)
                }
                .setNegativeButton("Hủy", { dialog, which ->
                    dialog.dismiss()
                })
            builder.show()
        }
    }

    private fun deleteProduct(id: Int, position: Int, context: Context?) {
        compositeDisposable.add(
            apiCafe.deleteProduct(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ productModel ->
                    if (productModel.isSuccess()) {
                        listProduct.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listProduct.size)
                        Toast.makeText(
                            context,
                            "Xóa đồ uống thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Xóa đồ uống không thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }, { throwable ->
                    Toast.makeText(
                        context,
                        "Lỗi : ${throwable.message}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
                )
        )
    }
}
