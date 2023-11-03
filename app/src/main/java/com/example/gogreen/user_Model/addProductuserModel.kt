package com.example.gogreen.user_Model

data class addProductuserModel(
    val productName : String?="",
    val productDescription : String?="",
    val productPrice : String?="",
    val productCoverImg : String?="",
    val productCategory : String?="",
    val productId : String?="",
    val productImages : ArrayList<String> = ArrayList()

)
