/*
 * SettingsHelper.kt
 * Um helper para o controle das credenciais utilizadas pelo App:
 *   * AppKey
 *   * Token
 *   * Hash (Sha)
 * Também possui a leitura do tipo de Usuário cadastrado no dispositivo.
 *
 * Utiliza o SharedPrefsLoader para não ter que ficar criando o objeto externamente sem parar.
 *
 */
package br.unicap.fullstack.minhamerenda.Helper

import android.content.Context
import br.unicap.fullstack.minhamerenda.R
import org.apache.commons.codec.digest.DigestUtils

class SettingsHelper {
    companion object {
        fun getAppKey(context: Context): String {
            val sharedPrefs:SharedPrefsLoader = SharedPrefsLoader(context.getString(R.string.prefs_file), context)
            return sharedPrefs.getString(context.getString(R.string.prefs_appkey))
        }

        fun getAppToken(context: Context): String {
            val sharedPrefs:SharedPrefsLoader = SharedPrefsLoader(context.getString(R.string.prefs_file), context)
            return sharedPrefs.getString(context.getString(R.string.prefs_apptoken))
        }

        fun getAppUsuario(context: Context): Long {
            val sharedPrefs:SharedPrefsLoader = SharedPrefsLoader(context.getString(R.string.prefs_file), context)
            return sharedPrefs.getLong(context.getString(R.string.prefs_appusuario))
        }

        fun getCredentialsAsMap(context:Context): Map<String,String> {
            val appKey:String = SettingsHelper.getAppKey(context)
            val appToken:String = SettingsHelper.getAppToken(context)

            return hashMapOf(
                    context.getString(R.string.prefs_appkey) to appKey,
                    context.getString(R.string.prefs_apptoken) to appToken
            )
        }

        fun hash(value:String): String = DigestUtils.shaHex(value)
    }
}