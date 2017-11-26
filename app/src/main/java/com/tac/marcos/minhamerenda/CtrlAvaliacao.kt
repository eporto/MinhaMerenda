package com.tac.marcos.minhamerenda

import android.util.Log
import com.example.marcos.okhttptest.Escola
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import java.net.SocketTimeoutException

/**
 * Created by Marcos on 25/11/2017.
 */
class CtrlAvaliacao() {
    private var client: OkHttpClient? = null
    private var request: Request? = null
    private val http = OkHttpClass()
    private val urlEscola: String = "minha-merenda.herokuapp.com/ts830/list/escola"
    private val urlAvaliacao: String = "minha-merenda.herokuapp.com/ts830/list/avaliacao"


    fun getAll(): ArrayList<Avaliacao>? {
        client = http.client
        request = http.getRequest(urlAvaliacao)
        var json = http.GETurl(client, request)
        var avaliacaoList = ArrayList<Avaliacao>()

        Log.i("JSON Avaliacao: ", json)
        try {
            var jsonresp = org.json.JSONArray(json)
            avaliacaoList.clear()
            for (i in 0 until jsonresp.length()) {
                var r = Avaliacao()
                var x = Escola()

                //Setting Escola
                var escolajson = jsonresp.getJSONObject(i).getJSONObject("escola")
                x.setEscolaID(escolajson.getInt("id"))
                x.setEscolaNome(escolajson.getString("escolaNome"))
                x.setLatitude(escolajson.getString("latitude"))
                x.setLongitude(escolajson.getString("longitude"))

                //Setting Avaliacao
                r.setAvaliacaoID(jsonresp.getJSONObject(i).getInt("id"))
                r.setPontuacao(jsonresp.getJSONObject(i).getInt("pontuacao"))
                r.setEscola(x)
                r.setFoto("link da foto")


                avaliacaoList.add(r)
            }
            return avaliacaoList

        }catch (e : JSONException){
//                        jsonTextField!!.text = json
        }catch (e : SocketTimeoutException){
//                        jsonTextField!!.text = e.message
        }

        return null
    }

    fun getAvaliacao(escola: Escola): ArrayList<Avaliacao>? {
        client = http.client
        request = http.getRequest(urlAvaliacao)
        var json = http.GETurl(client, request)
        var avaliacaoList = ArrayList<Avaliacao>()

        try {
            var jsonresp = org.json.JSONArray(json)
            avaliacaoList.clear()
            for (i in 0 until jsonresp.length()) {
                var r = Avaliacao()

                //Setting Escola
                var escolajson = jsonresp.getJSONObject(i).getJSONObject("escola")

                if(escola.getEscolaNome() == escolajson.getString("escolaNome")){
                    //Setting Avaliacao
                    r.setAvaliacaoID(jsonresp.getJSONObject(i).getInt("id"))
                    r.setPontuacao(jsonresp.getJSONObject(i).getInt("pontuacao"))
                    r.setEscola(escola)
                    r.setFoto("link da foto")

                    avaliacaoList.add(r)
                }

            }
            return avaliacaoList

        }catch (e : JSONException){
//                        jsonTextField!!.text = json
        }catch (e : SocketTimeoutException){
//                        jsonTextField!!.text = e.message
        }

        return null
    }
}