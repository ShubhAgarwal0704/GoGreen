package com.example.gogreen.admin_Model

data class addProductModel(
    val productName : String?="",
    val productDescription : String?="",
    val productCoverImg : String?="",
    val productCategory : String?="",
    val productId : String?="",
    val productPrice : String?="",
    val productImages : ArrayList<String>

)
