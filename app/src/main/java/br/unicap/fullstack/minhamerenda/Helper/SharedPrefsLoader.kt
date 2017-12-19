/*
 * SharedPrefsLoader.kt
 * Um helper para controle do Shared Preferences.
 *
 * Possui funções para escrita & leitura do Shared Preferences
 *
 */
package br.unicap.fullstack.minhamerenda.Helper

import android.content.Context
import android.content.SharedPreferences


//TODO #Review da pra fazer Static?
/**
 * Constructor
 * @param preferencesFileName : Nome do Arquivo do Shared Preferences (string.xml)
 * @param context : App Context
 */
class SharedPrefsLoader(val preferencesFileName: String, val context: Context) {
    val sharedPrefs: SharedPreferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);

    /* Escrita */
    fun add(key: String, value:Int): Boolean = this.sharedPrefs.edit().putInt(key, value).commit();
    fun add(key: String, value:Long): Boolean = this.sharedPrefs.edit().putLong(key, value).commit();
    fun add(key: String, value:String): Boolean = this.sharedPrefs.edit().putString(key, value).commit();
    fun add(key: String, value:Boolean): Boolean = this.sharedPrefs.edit().putBoolean(key, value).commit();

    //TODO #Review Kotlin Bug: Conflicting Overloads (Eu ia ter 4 funções chamadas getValue onde cada uma ia ter um parâmetro diferente: int, string, etc...)
    /* Leitura */
    fun getString(key:String): String = this.sharedPrefs.getString(key,"")
    fun getInt(key:String): Int = this.sharedPrefs.getInt(key,0)
    fun getLong(key:String): Long = this.sharedPrefs.getLong(key,0)
    fun getBoolean(key:String):Boolean = this.sharedPrefs.getBoolean(key,false);
    fun getBooleanWithDefault(key:String, default:Boolean):Boolean = this.sharedPrefs.getBoolean(key,default);

    /* Utils */
    companion object {
        fun clearData(preferencesFileName: String, context: Context) {
            val sharedPrefs: SharedPreferences = context.getSharedPreferences(preferencesFileName, Context.MODE_PRIVATE);
            sharedPrefs.edit().clear().commit()
        }
    }
}