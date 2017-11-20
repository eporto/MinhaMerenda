package com.tac.marcos.minhamerenda

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.example.marcos.okhttptest.Escola
import kotlinx.android.synthetic.main.activity_make_avaliacao.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class MakeAvaliacao : AppCompatActivity() {

    //VIEW
    var escolaTxt: TextView? = null
    var result: String? = null
    var pontuacaoStar: RatingBar? = null

    //Classes
    var escola: Escola? = null
    var user: Usuario? = null

    //OKHTTP
    private var client: OkHttpClient? = null
    private val http = OkHttpClass()
    var urlPostAvaliacao = "minha-merenda.herokuapp.com/ts830/create/avaliacao"

    //JSON
    var avaliacaoJson: JSONObject = JSONObject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_avaliacao)

        //VIEW
        escolaTxt = findViewById<View>(R.id.txtEscola) as TextView
        pontuacaoStar = findViewById<View>(R.id.starPontuacaoPost) as RatingBar

        //Getting Escola from previous Activity
        var bundle: Bundle = intent.extras
        escola = bundle.getSerializable("escola") as Escola?

        //Getting Avaliações from previus Activity
        user = bundle.getSerializable("user") as Usuario

        //Setting View
        escolaTxt!!.text = escola!!.getEscolaNome()

        Log.i("escolaID", escola!!.getEscolaId().toString())
        Log.i("Nome", escola!!.getEscolaNome())
        Log.i("Longitude", escola!!.getLongitude())
        Log.i("Latitude", escola!!.getLatitude())
        Log.i("userID", user!!.getUsuarioID().toString())
        Log.i("userTipo", user!!.getTipo())



        btnPost.setOnClickListener {
            view -> Snackbar.make(view, "Enviando Avaliação. Aguarde.", Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            //Making Avaliacao
            avaliacaoJson.put("escola", JSONObject().put("id", escola!!.getEscolaId()))
            avaliacaoJson.put("usuario", JSONObject().put("id", user!!.getUsuarioID()))
            avaliacaoJson.put("pontuacao", pontuacaoStar!!.rating)
            avaliacaoJson.put("foto", "Link da foto")
            Log.i("Pontuação", starPontuacaoPost.rating.toString())
            Log.i("JSON AVALIACAO", avaliacaoJson.toString())
            thread {
                try {
                    client = http.client
                    result = http.POSTurl(client, avaliacaoJson.toString(), urlPostAvaliacao)

                    this.runOnUiThread {
                        Snackbar.make(view, result.toString(), Snackbar.LENGTH_LONG).setAction("Action", null).show()
                        finish()
                    }

                }catch (e: IllegalArgumentException){
                    Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                }catch (b: SocketTimeoutException){
                    Snackbar.make(view, b.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                }
            }

        }

    }
}