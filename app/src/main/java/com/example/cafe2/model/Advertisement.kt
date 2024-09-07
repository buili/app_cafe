package com.example.cafe2.model

class Advertisement(private var id:Int,
    private var name:String,
    private var  image:String) {

    fun getId()=id
    fun setId(id:Int){
        this.id = id
    }

    fun getName() = name
    fun setName(name: String){
        this.name = name
    }

    fun getImage()= image
    fun setImage(image: String){
        this.image = image
    }
}