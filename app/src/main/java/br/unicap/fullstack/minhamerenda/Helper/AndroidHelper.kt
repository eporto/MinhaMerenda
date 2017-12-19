/*
 * AndroidHelper.kt
 * Um helper para dados relacionados ao Android
 *
 */
package br.unicap.fullstack.minhamerenda.Helper

import android.content.Context
import android.provider.Settings

/**
 * Created by shido on 07/12/2017.
 */
class AndroidHelper {
    companion object {
        //Android ID
        fun getAndroidId(context: Context): String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}