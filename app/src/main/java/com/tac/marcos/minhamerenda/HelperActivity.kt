package com.tac.marcos.minhamerenda

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.example.marcos.okhttptest.Escola
import okhttp3.OkHttpClient
import org.apache.commons.codec.digest.DigestUtils
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.concurrent.thread



/**
 * Created by Marcos on 18/11/2017.
 */
//TODO Setar as prefs de firstStart depois de salvar a escola na FirstStartTabTwo


class HelperActivity : AppCompatActivity() {
    private var prefs: SharedPreferences? = null
    private var ctrl_avaliacao = CtrlAvaliacao()
    private var AvaliacaoList: ArrayList<Avaliacao>? = null
    private var escola: Escola? = Escola()
    private var escolaJsonObj: JSONObject? = null
    private var appKey: String? = null
    private var androidID: String? = null
    private var loadingview: TextView? = null
    private var client: OkHttpClient? = null
    private val http = OkHttpClass()
    private var result: String? = null
    private var jsonPost: JSONObject = JSONObject();
    private val url = "minha-merenda.herokuapp.com/ts830/register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.helper_activity)

        prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref", "HardwareIds")
    override fun onResume() {
        super.onResume()
        loadingview = findViewById(R.id.txtLoading)

        if (prefs!!.getBoolean("firststart", true)) {
            //Do first Run Stuff
            Log.i("TAG", "Primeira vez")

            //Create appkey
            androidID = Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID)
            appKey = DigestUtils.md5Hex(androidID)
            prefs!!.edit().putBoolean("firststart", false).apply()
            prefs!!.edit().putString("appKey", appKey).apply()

            Log.i("androidID", androidID)
            Log.i("appkey", appKey)


            //POST the appKey to webservice
            //create Json with appKey
            jsonPost.put("appKey",appKey)

            thread {
                try{ //Make the Post to webservice
                    client = http.client
                    result = http.POSTurl(client, jsonPost.toString(),url)
                    while (result == null){
                        //Wait for response
                    }
                    //Save token generated from webservice to SharedPrefs
                    prefs!!.edit().putString("Token", result).apply()
                    Log.i("Token", result)

                }catch (e: IllegalArgumentException){

                }catch (b: SocketTimeoutException){

                }

            }


            startActivity(Intent(this, FirstStartTabOne::class.java))
            finish()
        }
        else{
            Log.i("TAG", "Não é primeira vez")

            var escolaJson = prefs!!.getString("escolaJson", "0")
            Log.i("escolaJson: ", escolaJson)

            escolaJsonObj = JSONObject(escolaJson)
            Log.i("escolaJsonObj: ", escolaJsonObj.toString())
            escola!!.run {
                setEscolaID(escolaJsonObj!!.getInt("id"))
                setEscolaNome(escolaJsonObj!!.getString("escolaNome"))
                setLatitude(escolaJsonObj!!.getString("latitude"))
                setLongitude(escolaJsonObj!!.getString("longitude"))
            }

            thread {
                try {
                    AvaliacaoList = ctrl_avaliacao.getAll()!!
                    while (AvaliacaoList == null){

                    }
                    var bundle = Bundle()
                    bundle.putSerializable("escola", escola)
                    bundle.putSerializable("avaliacao", AvaliacaoList)
                    startActivity(Intent(this, MainActivity::class.java).putExtras(bundle))
                    finish()
                }catch (e : IllegalArgumentException){
                    loadingview!!.text = e.message
                }catch (a : SocketTimeoutException){
                    loadingview!!.text = a.message
                }
            }

//            startActivity(Intent(this, FirstStartTabTwo::class.java))
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
//            finish()
        }
    }
}
