package com.example.gogreen.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


import androidx.annotation.NonNull


@Entity(tableName = "products")
data class ProductModel(
    @PrimaryKey
    @NonNull
    val productId: String,
    @ColumnInfo(name = "productName")
    val productName: String? = "",

    @ColumnInfo(name = "productImage")
    val productImage: String? = "",

    @ColumnInfo(name = "productprice")
    val productprice: String? = "",

    )
