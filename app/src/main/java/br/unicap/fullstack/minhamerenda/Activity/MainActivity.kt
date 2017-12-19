/*
 * MainActivity.kt
 * Mostra as informações de uma Escola (imagem) e a média de avaliações
 * Possibilidade da criar uma avaliação nova.
 *
 */

package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.Model.Avaliacao
import br.unicap.fullstack.minhamerenda.Model.Usuario
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.AvaliacaoRepository
import br.unicap.fullstack.minhamerenda.Service.UsuarioRepository
//import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.Serializable
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //setSupportActionBar(toolbar) //appbarmain


        val userId:Long = SettingsHelper.getAppUsuario(this@MainActivity)
        val user = Usuario(userId, UsuarioRepository.getUsuarioById(userId.toInt()))

        //Obtém a Escola da Activity anterior
        val bundle: Bundle = intent.extras
        val escola : Escola = bundle.getSerializable("escola") as Escola

        thread {
            val avaliacoes: List<Avaliacao> = listarAvaliacoesDaEscola(escola)
            val mediaAvaliacao: Float = calcularMedia(avaliacoes)

            runOnUiThread {
                this.txtEscola.text = escola.escolaNome
                if (mediaAvaliacao == 0F) {
                    this.txtPontuacao.text = "Ainda não possui avaliações"
                } else {
                    this.starPontuacaoPost.rating = mediaAvaliacao
                    this.txtPontuacao.text = String.format("%.01f", mediaAvaliacao) + " Estrelas"
                }
            }
        }

        //Cria uma nova avaliação (Chama outra activity)
        this.fab.setOnClickListener { _ ->
            var bundle = Bundle()
            bundle.putSerializable("escola", escola as Serializable)
            bundle.putSerializable("user", user as Serializable)
            startActivity(Intent(this, MakeAvaliacao::class.java).putExtras(bundle))
        }

    }

    private fun calcularMedia(avaliacaoList:List<Avaliacao>) : Float {
        var media: Float = 0F
        if(avaliacaoList.isEmpty())
            return 0F
        else {
            for (i in 0 until avaliacaoList.size) {
                media += avaliacaoList[i].pontuacao
            }

            return ( media / avaliacaoList.size )
        }
    }

    private fun listarAvaliacoesDaEscola(escola: Escola) : List<Avaliacao> {
        return  AvaliacaoRepository.listAvaliacaoByEscola(escola.id.toString(), this)
    }

//    override fun onBackPressed() {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        } else {
//            startActivity(Intent(this, FirstStartUserActivity::class.java))
//        }
//    }
}
