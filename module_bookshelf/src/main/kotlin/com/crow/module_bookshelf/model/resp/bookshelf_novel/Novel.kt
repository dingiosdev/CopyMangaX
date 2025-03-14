package com.crow.module_bookshelf.model.resp.bookshelf_novel


import com.crow.module_bookshelf.model.resp.bookshelf.Author
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Novel(

    @Json(name = "author")
    val mAuthor: List<Author>,

    @Json(name = "b_display")
    val mBDisplay: Boolean,

    @Json(name = "cover")
    val mCover: String,

    @Json(name = "datetime_updated")
    val mDatetimeUpdated: String,

    @Json(name = "last_chapter_id")
    val mLastChapterId: String,

    @Json(name = "last_chapter_name")
    val mLastChapterName: String,

    @Json(name = "name")
    val mName: String,

    @Json(name = "path_word")
    val mPathWord: String,

    @Json(name = "popular")
    val mPopular: Int,

    @Json(name = "status")
    val mStatus: Int,

    @Json(name = "theme")
    val mTheme: List<Any>,

    @Json(name = "uuid")
    val mUuid: String
)