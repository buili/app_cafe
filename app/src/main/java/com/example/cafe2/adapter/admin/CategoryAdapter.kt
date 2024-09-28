package com.example.cafe2.adapter.admin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.cafe2.R
import com.example.cafe2.databinding.ItemCategoryBinding
import com.example.cafe2.model.admin.Category
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import com.example.cafe2.view.activity.admin.AddCategoryActivity
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class CategoryAdapter(val listCategory: MutableList<Category>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }

    class ViewHolder(val itemCategoryBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(itemCategoryBinding.root) {
        init {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Category = listCategory[position]
        holder.itemCategoryBinding.txtNameCategory.text = item.nameCategory

        val context = holder.itemView.context
        holder.itemCategoryBinding.imgEditCategory.setOnClickListener {
            val intent = Intent(context, AddCategoryActivity::class.java)
            intent.putExtra("editCategory", item)
            context.startActivity(intent)
        }
        holder.itemCategoryBinding.imgDeleteCategory.setOnClickListener {
            var builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Thông báo")
                .setIcon(R.drawable.baseline_announcement_24)
                .setMessage("Bạn có muốn xóa loại sản phẩm không")
                .setPositiveButton("Đồng ý") { dialog, which ->
                    deleteCategory(item.id, position, context)
                }
                .setNegativeButton("Hủy", { dialog, which ->
                    dialog.dismiss()
                })
            builder.show()
        }
    }

    fun deleteCategory(idCategory: Int, position: Int, context: Context) {
        compositeDisposable.add(
            apiCafe.deleteCategory(idCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ categoryModel ->
                    if (categoryModel.isSuccess()) {
                        listCategory.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, listCategory.size)
                        Toast.makeText(
                            context,
                            "Xóa loại đồ uống thành công",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            context,
                            "Xóa loại đồ uống không thành công",
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