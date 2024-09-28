package com.example.cafe2.view.activity.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.databinding.DataBindingUtil
import com.example.cafe2.R
import com.example.cafe2.databinding.ActivityAddCategoryBinding
import com.example.cafe2.model.admin.Category
import com.example.cafe2.retrofit.ApiCafe
import com.example.cafe2.retrofit.RetrofitClient
import com.example.cafe2.util.Utils
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class AddCategoryActivity : AppCompatActivity() {
    private lateinit var addCategoryBinding: ActivityAddCategoryBinding
    private lateinit var edtNameCategory: EditText
    private lateinit var btnAddCategory: AppCompatButton
    private val compositeDisposable = CompositeDisposable()
    private val apiCafe by lazy {
        RetrofitClient.getInstance(Utils.BASE_URL).create(ApiCafe::class.java)
    }
    var flag = false
    private var category: Category? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_add_category)
        addCategoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_category)


        initView()
        initControl()

        category = intent.getSerializableExtra("editCategory") as? Category
        if (category == null) {
            flag = false
        } else {
            flag = true
            btnAddCategory.text = "Sửa thể loại"
            //  edtNameCategory.text = Category.nameCategory
            edtNameCategory.text =
                Editable.Factory.getInstance().newEditable(category!!.nameCategory)
        }
    }

    private fun initControl() {
        btnAddCategory.setOnClickListener {
            if (flag == true) {
                editCategory()
            } else {
                addCategory()
            }
        }

    }

    private fun editCategory() {

        var str_nameCategory = edtNameCategory.text.toString().trim()
        if (!TextUtils.isEmpty(str_nameCategory)) {
            compositeDisposable.add(apiCafe.editCategory(category?.id, str_nameCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { categoryModel ->
                        if (categoryModel.isSuccess()) {
                            Toast.makeText(
                                applicationContext,
                                "Sửa đồ uống thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Sửa không thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { throwable ->
                        Toast.makeText(
                            applicationContext,
                            "Sửa không thành công",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                )
            )
        }
    }


    private fun addCategory() {

        var str_nameCategory = edtNameCategory.text.toString().trim()
        if (!TextUtils.isEmpty(str_nameCategory)) {
            compositeDisposable.add(apiCafe.addCategory(str_nameCategory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { categoryModel ->
                        if (categoryModel.isSuccess()) {
                            Toast.makeText(
                                applicationContext,
                                "Thêm loại đồ uống thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                applicationContext,
                                "Thêm không thành công",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    { throwable ->
                        Toast.makeText(
                            applicationContext,
                            "Thêm không thành công",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                )
            )
        }

    }


    private fun initView() {
        edtNameCategory = addCategoryBinding.edtNameCategory
        btnAddCategory = addCategoryBinding.btnAddCategory
    }

}