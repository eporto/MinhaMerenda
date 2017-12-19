/*
 * RegisterRepository.kt
 * Controle do Registro de novos aparelhos
 *
 */
package br.unicap.fullstack.minhamerenda.Service

import android.content.Context
import android.util.Log
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.R
import okhttp3.Response
import org.json.JSONObject

class RegisterRepository {
    companion object {
        fun registerAccount(context:Context) : String? {
            val appKey : String = SettingsHelper.getAppKey(context)
            val appKeyJson : JSONObject = JSONObject().put("appKey", appKey)

            val httpService: HttpService = HttpService()
            val response : Response = httpService.doPost(context.getString(R.string.endpoint_register), appKeyJson.toString())

            if(response.code() != 200) {
                return ""
            } else
                return response.body()?.string()
        }
    }
}