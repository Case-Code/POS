package com.casecode.domain.model

data class PlanUsed(
    val documentsUsed: DocumentsUsed,
    val durationUsed: Int,
    val planCode: Int,
    val planName: String
)