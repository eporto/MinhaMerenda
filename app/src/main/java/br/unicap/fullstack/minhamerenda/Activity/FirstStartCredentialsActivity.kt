/*
 * FirstStartCredentialsActivity.kt
 * Responsável por fazer baixar a lista de escolas, coordenadas do servidor e salvá-las no dispositivo (Sqlite)
 *
 */

package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.marcos.okhttptest.EscolaRepository
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Data.EscolaGeometry
import br.unicap.fullstack.minhamerenda.Helper.AndroidHelper
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.Helper.SharedPrefsLoader
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.HttpService
import br.unicap.fullstack.minhamerenda.Service.RegisterRepository
import kotlinx.android.synthetic.main.activity_first_start_credentials.*
import org.json.JSONObject
import kotlin.concurrent.thread


class FirstStartCredentialsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_start_credentials)
    }

    override fun onResume() {
        super.onResume()

        onFirstStart()
    }

    private fun onFirstStart() {
        //SharedPrefsLoader & SettingsHelper & AndroidHelper são Helper classes para ajudar no manage de operações especificas
        val sharedPrefs:SharedPrefsLoader = SharedPrefsLoader(this.getString(R.string.prefs_file),this@FirstStartCredentialsActivity)

        //Obtém Android ID e utiliza um Hash (SHA) em cima. Utiliza o resultado como AppKey do dispositivo
        val androidId: String = AndroidHelper.getAndroidId(this@FirstStartCredentialsActivity)
        val appKey: String = SettingsHelper.hash(androidId)

        //Salva a recém AppKey no SharedPrefs e cria um campo isFirstStart = false
        sharedPrefs.add(this.getString(R.string.prefs_is_first_start), false) //TODO Isso deveria estar na outra activity.
        sharedPrefs.add(this.getString(R.string.prefs_appkey), appKey)

        //Registra no servidor através de um POST { appkey }
        //Recebe um Token do servidor como resposta ao POST
        //Se o token for diferente de null, salva o token no sharedprefs (Algum erro interno no servidor)
        thread {
            try{
                val token:String? = RegisterRepository.registerAccount(this)

                if(token != null && token != "") {
                    sharedPrefs.add(getString(R.string.prefs_apptoken), token)
                    insertEscolaToDB()
                }
                else {
                    onErrorFirstStart("Response Null")
                }
            }catch (e: Exception) {
                onErrorFirstStart(e.message)
            }
        }
    }

    //TODO (Shido) Fazer o onErrorFirstStart
    private fun onErrorFirstStart(msg:String?) {
        Log.i("onErrorFirstStart",msg)
    }

    //Obtém a lista de escolas do Server e salva no dispositivo (SQL)
    private fun insertEscolaToDB() {
        val escolaList:List<Escola> = EscolaRepository.getEscolaFromServer(this@FirstStartCredentialsActivity)
        val escolasInserted:LongArray? = EscolaRepository.insertEscolas(escolaList, this@FirstStartCredentialsActivity)

        //Uma vez que tiver recebido a lista de escolas, fazer o mesmo com as coordenadas
        if(escolasInserted != null && escolasInserted.size > 0) {
            val geometryList:List<EscolaGeometry> = EscolaRepository.getEscolaGeometryFromServer(this@FirstStartCredentialsActivity)
            val geometryInserted:LongArray? = EscolaRepository.insertGeometries(geometryList, this@FirstStartCredentialsActivity)

            if(geometryInserted != null && geometryInserted.size > 0) {
                initButtonUi() //Tudo Ok, pode exibir o botão
            } else
                onErrorFirstStart("Error Insert Geometry to DB")
        } else
            onErrorFirstStart("Error Insert Escola to DB")
    }

    //Botão estava invisivel até agora, só será exibido quando tudo estiver Ok
    private fun initButtonUi() {
        runOnUiThread({
            this.btntab1.visibility = View.VISIBLE
        })

        this.btntab1.setOnClickListener {_ ->
            startActivity(Intent(this, FirstStartUserActivity::class.java))
        }
    }
}
