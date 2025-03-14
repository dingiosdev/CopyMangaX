package com.crow.module_book.model.resp


import com.crow.module_book.model.resp.novel_browser.Browse
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NovelBrowserResp(

    @Json(name = "browse")
    val mBrowse: Browse?,

    @Json(name = "collect")
    val mCollect: Int?,

    @Json(name = "is_lock")
    val mIsLock: Boolean,

    @Json(name = "is_login")
    val mIsLogin: Boolean,

    @Json(name = "is_mobile_bind")
    val mIsMobileBind: Boolean,

    @Json(name = "is_vip")
    val mIsVip: Boolean
)