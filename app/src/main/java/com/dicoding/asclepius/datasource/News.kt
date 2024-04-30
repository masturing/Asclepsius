package com.dicoding.asclepius.datasource

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class News(
    val status: String?,
    val totalResults: Long?,
    val articles: List<Article>?,
) : Parcelable

