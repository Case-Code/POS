package com.casecode.domain.model

data class Invoice(
    val createdBy: Int,
    val invoiceDate: String,
    val invoiceNumber: Int,
    val items: List<Item>
)