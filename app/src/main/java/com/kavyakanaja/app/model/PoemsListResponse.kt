package com.kavyakanaja.app.model

import com.google.gson.annotations.SerializedName

/**
 * Paginated response from GET /poems
 */
data class PoemsListResponse(
    @SerializedName("poems")       val poems: List<PoemData>,
    @SerializedName("total")       val total: Int,
    @SerializedName("page")        val page: Int,
    @SerializedName("page_size")   val pageSize: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("has_next")    val hasNext: Boolean,
    @SerializedName("has_prev")    val hasPrev: Boolean
)