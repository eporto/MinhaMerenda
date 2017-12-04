package com.tac.marcos.minhamerenda

import android.content.SharedPreferences
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
    private var Appkey: String? = null
    private var token: String? = null



    fun getAll(p : SharedPreferences): ArrayList<Avaliacao>? {
        Appkey = p.getString("appKey", "0")
        token = p.getString("Token", "0")
        client = http.client
        request = http.getRequestHeader(urlAvaliacao, Appkey, token)
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
                x.setEscolaCodigo(escolajson.getInt("escolaCodigo"))
                x.setEscolaNome(escolajson.getString("escolaNome"))
                x.setEndereco(escolajson.getString("endereco"))
                x.setMecCodigo(escolajson.getString("mecCodigo"))

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

    fun getAvaliacao(escola: Escola, p: SharedPreferences): ArrayList<Avaliacao>? {
        Appkey = p.getString("appKey", "0")
        token = p.getString("Token", "0")
        client = http.client
        request = http.getRequestHeader(urlAvaliacao, Appkey, token)
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