package com.tac.marcos.minhamerenda

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.marcos.okhttptest.Escola
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

/**
 * Created by Marcos on 18/11/2017.
 */



class HelperActivity : AppCompatActivity() {
    var prefs: SharedPreferences? = null
    var ctrl_avaliacao = CtrlAvaliacao()
    var AvaliacaoList = ArrayList<Avaliacao>()
    var escola: Escola? = Escola()
    var escolaJsonObj: JSONObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.helper_activity)

        prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
    }

    @SuppressLint("ApplySharedPref")
    override fun onResume() {
        super.onResume()

        if (prefs!!.getBoolean("firststart", true)) {
            //Do first Run Stuff
            Log.i("TAG", "Primeira vez")

            prefs!!.edit().putBoolean("firststart", false).apply()
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
                    while (AvaliacaoList.isEmpty()){

                    }
                    var bundle = Bundle()
                    bundle.putSerializable("escola", escola)
                    bundle.putSerializable("avaliacao", AvaliacaoList)
                    startActivity(Intent(this, MainActivity::class.java).putExtras(bundle))
                    finish()
                }catch (e : IllegalArgumentException){
//                    jsonTextField!!.text = e.message
                }catch (a : SocketTimeoutException){
//                    jsonTextField!!.text = a.message
                }
            }

//            startActivity(Intent(this, FirstStartTabTwo::class.java))
//            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
//            finish()
        }
    }
}
