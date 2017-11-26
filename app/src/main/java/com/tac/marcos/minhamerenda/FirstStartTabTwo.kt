package com.tac.marcos.minhamerenda

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.example.marcos.okhttptest.Escola
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class FirstStartTabTwo : AppCompatActivity() {

    //VIEW
    private var btnNext: FloatingActionButton? = null
    private var cbEscolas: Spinner? = null

    //OKHTTP
    private var client: OkHttpClient? = null
    private var request: Request? = null
    private val http = OkHttpClass()
    private val urlEscola: String = "minha-merenda.herokuapp.com/ts830/list/escola"
    private val urlAvaliacao: String = "minha-merenda.herokuapp.com/ts830/list/avaliacao"

    //CLASSES TO MAP
    private val escolaList = ArrayList<Escola>()
    private var escolaToSend: Escola? = null
    private var avaliacaoList = ArrayList<Avaliacao>()
//    private var avaliacaoToSend: Avaliacao? = null

    //Controllers
    private val ctrl_avaliacao = CtrlAvaliacao()

    //JSON RETURN
    private var json: String? = null
    private var jsonresp = JSONArray()

    //Shared Preferences
    private var prefs: SharedPreferences? = null


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_start_tab2)
        //View
        btnNext = findViewById<View>(R.id.btntab2) as FloatingActionButton
        cbEscolas = findViewById<View>(R.id.comboBox) as Spinner


        //Getting Escolas from WebServer
        thread {

            try{

                client = http.client
                request = http.getRequest(urlEscola)
                json = http.GETurl(client, request)

                Log.i("JSON", json)

                this.runOnUiThread {

                    try {
                        jsonresp = org.json.JSONArray(json)
                        escolaList.clear()
                        for (i in 0 until jsonresp.length()) {
                            var r = Escola()
                            r.setEscolaID(jsonresp.getJSONObject(i).getInt("id"))
                            r.setEscolaNome(jsonresp.getJSONObject(i).getString("escolaNome"))
                            r.setLatitude(jsonresp.getJSONObject(i).getString("latitude"))
                            r.setLongitude(jsonresp.getJSONObject(i).getString("longitude"))


                            escolaList.add(r)
                        }

                        val escolaString = ArrayList<String>()
                        for (i in escolaList){
                            android.util.Log.i("TAG", i.getEscolaNome())
                            escolaString.add(i.getEscolaNome().toString())
                        }


                        cbEscolas!!.adapter = android.widget.ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, escolaString)

                        cbEscolas!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
//                                jsonTextField!!.text = "Selecione a sua escola"
                            }

                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                escolaToSend = escolaList[position]
                            }

                        }

                    }catch (e : JSONException){
//                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                    }catch (e : SocketTimeoutException){
//                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                    }
                }

            }catch (e : IllegalArgumentException){
//                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }catch (a : SocketTimeoutException){
//                Snackbar.make(view, a.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }

            //Getting Avaliacao From WebServer
            try{
                avaliacaoList = ctrl_avaliacao.getAll()!!

//                client = http.client
//                request = http.getRequest(urlAvaliacao)
//                json = http.GETurl(client, request)
//
//                Log.i("JSON", json)
//
//                try {
//                    jsonresp = org.json.JSONArray(json)
//                    avaliacaoList.clear()
//                    for (i in 0 until jsonresp.length()) {
//                        var r = Avaliacao()
//                        var x = Escola()
//
//                        //Setting Escola
//                        var escolajson = jsonresp.getJSONObject(i).getJSONObject("escola")
//                        x.setEscolaID(escolajson.getInt("id"))
//                        x.setEscolaNome(escolajson.getString("escolaNome"))
//                        x.setLatitude(escolajson.getString("latitude"))
//                        x.setLongitude(escolajson.getString("longitude"))
//
//                        //Setting Avaliacao
//                        r.setAvaliacaoID(jsonresp.getJSONObject(i).getInt("id"))
//                        r.setPontuacao(jsonresp.getJSONObject(i).getInt("pontuacao"))
//                        r.setEscola(x)
//                        r.setFoto("link da foto")
//
//
//                        avaliacaoList.add(r)
//                    }
//
//                }catch (e : JSONException){
////                        jsonTextField!!.text = json
//                }catch (e : SocketTimeoutException){
////                        jsonTextField!!.text = e.message
//                }
//

            }catch (e : IllegalArgumentException){
//                jsonTextField!!.text = e.message
            }catch (a : SocketTimeoutException){
//                jsonTextField!!.text = a.message
            }
        }

        //Listenners
        btnNext!!.setOnClickListener{
            //save Escola into SharedPrefs
            var jsonEscola = JSONObject()
            jsonEscola.put("id", escolaToSend!!.getEscolaId()!!)
            jsonEscola.put("escolaNome", escolaToSend!!.getEscolaNome()!!)
            jsonEscola.put("longitude", escolaToSend!!.getLongitude()!!)
            jsonEscola.put("latitude", escolaToSend!!.getLatitude()!!)

            prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
            prefs!!.edit().putString("escolaJson", jsonEscola.toString()).apply()

            var escolaJson = prefs!!.getString("escolaJson", "0")
            Log.i("escolaJson: ", escolaJson)


            var bundle = Bundle()
            bundle.putSerializable("escola", escolaToSend)
            bundle.putSerializable("avaliacao", avaliacaoList)
            startActivity(Intent(this, MainActivity::class.java).putExtras(bundle))
            finish()
        }
    }

//    override fun onBackPressed() {
//
//        startActivity(Intent(this, FirstStartTabTwo::class.java))
//    }

}