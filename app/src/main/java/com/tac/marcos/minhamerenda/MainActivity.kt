package com.tac.marcos.minhamerenda

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RatingBar
import android.widget.TextView
import com.example.marcos.okhttptest.Escola
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val TAG: String = "MainActivity"

    //VIEW
    var escolaTxt: TextView? = null
    var pontuacaoTxt: TextView? = null
    var pontuacaoStar: RatingBar? = null
    var settings: MenuItem? = null

    //Classes
    var escola: Escola? = null
    var avaliacao: Avaliacao? = null
    var mediaAvaliacao: Float? = null
    var avaliacoes: ArrayList<Avaliacao>? = null
    var user: Usuario? = null
    var ctrl_avaliacao = CtrlAvaliacao()

    //JSON
    var escolaJsonObj: JSONObject? = null
    var avaliacaoJsonArray: JSONArray? = null

    //SharedPrefs
    private var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //VIEW
//        escolaTxt = findViewById<View>(R.id.txtEscola) as TextView
        escolaTxt = findViewById(R.id.txtEscola)
//        pontuacaoTxt = findViewById<View>(R.id.txtPontuacao) as TextView
        pontuacaoTxt = findViewById(R.id.txtPontuacao)
//        pontuacaoStar = findViewById<View>(R.id.starPontuacaoPost) as RatingBar
        pontuacaoStar = findViewById(R.id.starPontuacaoPost)
        //settings = findViewById<View>(R.id.action_settings) as MenuItem

//        Log.i("Hash", hashCode().toString())

        //Setting User
        user = Usuario(0, "Aluno")

        //Get values from SharedPrefs
        var bundle: Bundle = intent.extras
        //Getting Escola from previous Activity
        escola = bundle.getSerializable("escola") as Escola?

        //Getting Avaliações from previus Activity
        avaliacoes = bundle.getSerializable("avaliacao") as ArrayList<Avaliacao>

//        else{// Its not the first time. take Escolas and Avaliações from SharedPrefs
//            prefs = this.getSharedPreferences("mypref", Context.MODE_PRIVATE)
//            //setting Escola
//            var escolaJson = prefs!!.getString("escolaJson", "0")
//            if(escolaJson != "0"){
//                escolaJsonObj = JSONObject(escolaJson)
//                escola!!.setEscolaID(escolaJsonObj!!.getInt("id"))
//                escola!!.setEscolaNome(escolaJsonObj!!.getString("escolaNome"))
//                escola!!.setLatitude(escolaJsonObj!!.getString("latitude"))
//                escola!!.setLongitude(escolaJsonObj!!.getString("longitude"))
//            }
//
//            //Setting Avaliações
//            thread {
//                try {
//                    avaliacoes = ctrl_avaliacao.getAvaliacao(escola!!)
//                }catch (e : IllegalArgumentException){
////                    jsonTextField!!.text = e.message
//                }catch (a : SocketTimeoutException){
////                    jsonTextField!!.text = a.message
//                }
//            }
//
////            var avaliacaoJson = prefs!!.getString("avaliacaoJson", "0")
////            if(avaliacaoJson != "0"){
////                avaliacaoJsonArray = JSONArray(avaliacaoJson)
////
////                for (i in 0 until avaliacaoJsonArray!!.length()) {
////                    var r = Avaliacao()
////
////                    //Setting Avaliacao
////                    r.setAvaliacaoID(avaliacaoJsonArray!!.getJSONObject(i).getInt("id"))
////                    r.setPontuacao(avaliacaoJsonArray!!.getJSONObject(i).getInt("pontuacao"))
////                    r.setEscola(escola)
////                    r.setFoto("link da foto")
////
////                    avaliacoes!!.add(r)
////                }
////            }
//        }
        //Logs
        Log.i("Escola", escola!!.getEscolaNome())

        //Setting View
        escolaTxt!!.text = escola!!.getEscolaNome()
        mediaAvaliacao = escola!!.getAvaliacao(avaliacoes!!)

        if(mediaAvaliacao == null){
            pontuacaoTxt!!.text = "Ainda não possui avaliações"
        }
        else{
            pontuacaoStar!!.rating = mediaAvaliacao as Float
            pontuacaoTxt!!.text = String.format("%.01f", mediaAvaliacao)+" Estrelas"
        }

        //Listeners

        fab.setOnClickListener { view ->
            var bundle = Bundle()
            bundle.putSerializable("escola", escola)
            bundle.putSerializable("user", user)
            startActivity(Intent(this, MakeAvaliacao::class.java).putExtras(bundle))
//            finish()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

    }


//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            startActivity(Intent(this, FirstStartTabTwo::class.java))
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, FirstStartTabTwo::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
