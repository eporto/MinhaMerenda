/*
 * InitActivity.kt
 * (Main Activity)
 * Responsável por fazer o controle do FirstTime e Not-FirstTime.
 * Se for a primeira vez, chama a Activity responsável por fazer o download das escolas, coordenadas e escolha do tipo de usuário
 *
 */
package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.unicap.fullstack.minhamerenda.Data.AppDatabase
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Data.EscolaGeometry
import br.unicap.fullstack.minhamerenda.Helper.AndroidHelper
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.Helper.SharedPrefsLoader
import br.unicap.fullstack.minhamerenda.Model.Avaliacao
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.AvaliacaoRepository
import br.unicap.fullstack.minhamerenda.Service.HttpService
import org.json.JSONObject
import java.io.Serializable
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class InitActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.init_activity)

        //debuglol()
    }

    //TODO #Review Funciona melhor como AsyncTask?
    override fun onResume() {
        super.onResume()

        //Carregando o sharedPrefs e verificando se ele possui uma key "isFirstStart".
        //Tem como valor default "true", caso não encontre a key.
        val sharedPrefs = SharedPrefsLoader(this.getString(R.string.prefs_file), this@InitActivity)
        val isFirstStart: Boolean = sharedPrefs.getBooleanWithDefault(this.getString(R.string.prefs_is_first_start), true)

        if (isFirstStart) {
            //onFirstStart(sharedPrefs)

            startActivity(Intent(this, FirstStartCredentialsActivity::class.java))
            finish() //Activity.finish() = call(Activity.onDestroy())
        } else {
            //Main Page
            startActivity(Intent(this, LocalizarEscolaActivity::class.java))
            overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up)
            finish()
        }
    }

    //Só pra debugagens
    private fun debuglol() {
        SharedPrefsLoader.clearData(getString(R.string.prefs_file), this)

        //Precisa fazer isso no celular pra testar localização
        thread {
            val escolaDummy = Escola(
                    999,
                    900,
                    "Escola Dummy",
                    "Endereço Dummy",
                    9999
            )

            val geometryDummy = EscolaGeometry(
                    3000,
                    999,
                    "-34.88917840000000", ////-34.88930419826821
                    "-8.10982990000000" //-8.108108108108109
            )

            val manager = AppDatabase.getDatabase(this)?.escolaDao()
            manager?.addEscola(escolaDummy)
            manager?.addEscolaGeometry(geometryDummy)
        }

    }
}
