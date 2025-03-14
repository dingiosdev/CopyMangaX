
package com.crow.base.copymanga.entity

import com.crow.base.app.appContext
import com.crow.base.copymanga.BaseStrings
import com.crow.base.copymanga.BaseUser
import com.crow.base.tools.extensions.DataStoreAgent
import com.crow.base.tools.extensions.appConfigDataStore
import com.crow.base.tools.extensions.asyncDecode
import com.crow.base.tools.extensions.asyncEncode
import com.crow.base.tools.extensions.decode
import com.crow.base.tools.extensions.toJson
import com.crow.base.tools.extensions.toTypeEntity
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AppConfigEntity(

    /** ● 第一次初始化 */
    val mAppFirstInit: Boolean = false,

    /** ● 站点 */
    val mSite: String = BaseStrings.URL.CopyManga,

    /** ● 路线 "0", "1" */
    val mRoute: String = BaseUser.CURRENT_ROUTE,


) {
    companion object {

        private var mAppConfigEntity: AppConfigEntity? =null

        fun getInstance(): AppConfigEntity {
            return mAppConfigEntity!!
        }

        suspend fun saveAppConfig(appConfigEntity: AppConfigEntity) {
            mAppConfigEntity = appConfigEntity
            appContext.appConfigDataStore.asyncEncode(DataStoreAgent.APP_CONFIG, toJson(appConfigEntity))
        }

        suspend fun readAppConfig(): AppConfigEntity? {
            return appContext.appConfigDataStore.asyncDecode(DataStoreAgent.APP_CONFIG).toTypeEntity<AppConfigEntity>().also { mAppConfigEntity = it }
        }

        fun readAppConfigSync(): AppConfigEntity? {
            return appContext.appConfigDataStore.decode(DataStoreAgent.APP_CONFIG).toTypeEntity<AppConfigEntity>().also { mAppConfigEntity = it }
        }
    }
}
