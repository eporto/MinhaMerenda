package com.tac.marcos.minhamerenda

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RatingBar
import android.widget.TextView
import com.example.marcos.okhttptest.Escola
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //VIEW
        escolaTxt = findViewById<View>(R.id.txtEscola) as TextView
        pontuacaoTxt = findViewById<View>(R.id.txtPontuacao) as TextView
        pontuacaoStar = findViewById<View>(R.id.starPontuacaoPost) as RatingBar
        settings = findViewById<View>(R.id.action_settings) as MenuItem

        //Setting User
        user = Usuario(0, "Aluno")

        //Getting Escola from previous Activity
        var bundle: Bundle = intent.extras
        escola = bundle.getSerializable("escola") as Escola?

        //Getting Avaliações from previus Activity
        avaliacoes = bundle.getSerializable("avaliacao") as ArrayList<Avaliacao>

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

        settings!!.setOnMenuItemClickListener {
            
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            startActivity(Intent(this, FirstStartTabTwo::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
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
