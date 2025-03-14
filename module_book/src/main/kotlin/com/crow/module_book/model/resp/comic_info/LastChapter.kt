package com.crow.module_book.model.resp.comic_info


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LastChapter(

    @Json(name = "name") val mName: String,

    @Json(name = "uuid") val mUuid: String,
)