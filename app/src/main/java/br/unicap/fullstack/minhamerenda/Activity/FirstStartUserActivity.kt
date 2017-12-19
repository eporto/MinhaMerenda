/*
 * FirstStartUserActivity.kt
 * Baixa a lista de usuários do Servidor e insere em um Combobox. O usuário escolhe o tipo de usuário e muda para a outra Activity.
 * Depois disso essas 3 últimas activities não serão mais carregadas (isFirstStart = false)
 *
 */

package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import br.unicap.fullstack.minhamerenda.Service.HttpService
import br.unicap.fullstack.minhamerenda.Helper.JsonParser
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.Helper.SharedPrefsLoader
import br.unicap.fullstack.minhamerenda.Model.Usuario
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.UsuarioRepository
import kotlinx.android.synthetic.main.activity_first_start_user.*
import org.json.JSONArray
import org.json.JSONException
import java.io.Serializable
import java.net.SocketTimeoutException
import kotlin.concurrent.thread

class FirstStartUserActivity : AppCompatActivity() {
    private var selectedUser:Usuario? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_start_user)

        thread {
            try{

                val usuarioList:List<Usuario> = UsuarioRepository.getUsuarioFromServer(this)

                this.runOnUiThread {
                    try {
                        //Transforma o List<Usuario> em Strings para o combobox
                        this.comboBox.adapter = android.widget.ArrayAdapter<Usuario>(this, android.R.layout.simple_spinner_item, usuarioList)

                        this.comboBox.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                Log.i("firstarttabtwo","Selecione sua escola")
//                                jsonTextField!!.text = "Selecione a sua escola"
                            }

                            //Envia a escola selecionada, escolaList[pos] para a próxima activity
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                (parent!!.getChildAt(0) as TextView).setTextColor(Color.WHITE)
                                (parent.getChildAt(0) as TextView).textSize = 18f
                                selectedUser = usuarioList[position]
                            }

                        }

                    }catch (e : JSONException){
//                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                    }catch (e : SocketTimeoutException){
//                        Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
                    }
                }

            }catch (e : Exception){
//                Snackbar.make(view, e.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }catch (a : Exception){
//                Snackbar.make(view, a.message.toString(), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show()
            }
        }

        this.btntab2.setOnClickListener{
            if(selectedUser != null) {
                val sharedPrefs:SharedPrefsLoader = SharedPrefsLoader(this.getString(R.string.prefs_file),this@FirstStartUserActivity)
                if(selectedUser?.id != null) {
                    sharedPrefs.add(this.getString(R.string.prefs_appusuario), selectedUser!!.id)
                }

                startActivity(Intent(this, LocalizarEscolaActivity::class.java))
                finish()

            } else
                Log.i("select usuario", "Usuario nullo")

        }
    }

//    override fun onBackPressed() {
//
//        startActivity(Intent(this, FirstStartUserActivity::class.java))
//    }

}