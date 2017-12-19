/*
 * LocalizarEscolaActivity.kt
 * Activity principal depois da resolução do firstStart e Not-firstStart
 * Faz a busca de uma escola por GPS ou digitando.
 * Todas as informações das escolas estão salvas no dispositivo para não ter que perder tempo pedindo informação no servidor
 *
 */

package br.unicap.fullstack.minhamerenda.Activity

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.AdapterView
import com.example.marcos.okhttptest.EscolaRepository
import br.unicap.fullstack.minhamerenda.Data.AppDatabase
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Data.EscolaGeometry
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.LocalizationService
import kotlinx.android.synthetic.main.activity_localizar_escola.*
import java.io.Serializable
import kotlin.concurrent.thread
import android.view.inputmethod.EditorInfo

//TODO Fazer uma UI para esta Activity (A atual é um placeholder)
class LocalizarEscolaActivity : AppCompatActivity() {
    private var selectedEscola:Escola? = null
    private var escolas:List<Escola>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localizar_escola)

        initActivityButtons() //Listeners
    }

    override fun onResume() {
        super.onResume()
        thread{
            this.escolas = EscolaRepository.listAll(this) //Acesso ao banco
            runOnUiThread({
                initEditTextAutoComplete() //Depende do acesso ao banco
            })
        }
    }

    //ListView Adapter
    private fun initEscolaView(escolas:List<Escola>) {
        this.escolaView.adapter = android.widget.ArrayAdapter<Escola>(this, android.R.layout.simple_spinner_item, escolas)

        this.escolaView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            selectedEscola = parent.getItemAtPosition(position) as Escola
            Log.i("Escola:",selectedEscola?.escolaNome)
        }
    }

    private fun initEditTextAutoComplete() {
        this.editText.setAdapter(android.widget.ArrayAdapter<Escola>(this, android.R.layout.simple_list_item_1, escolas))
    }


    //Procura todas as Escolas que correspondem ao nome inserido no edit text
    private fun searchEscola() {
        val escolas: List<Escola>? = EscolaRepository.getEscolaByName(this.editText.text.toString(), this)

        if (escolas != null) {
            runOnUiThread{
                initEscolaView(escolas)
            }
        }
    }

    private fun initActivityButtons() {
        //Botão de Procura no teclado do edittext
        this.editText.setOnEditorActionListener() { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> { thread { searchEscola() }; true }
                else -> false
            }
        }

        //Próxima Activity
        this.btnNext.setOnClickListener({_ ->
            val bundle: Bundle = Bundle()
            bundle.putSerializable("escola", selectedEscola as Serializable)

            startActivity(Intent(this, MainActivity::class.java).putExtras(bundle))
            finish()
        })

        this.btnCurrentLocalize.setOnClickListener { _ ->
            getCurrentLocation()
        }

        this.btnLastLocalize.setOnClickListener { _ ->
            getLastLocation()
        }

        this.btnSearch.setOnClickListener ({ _ ->
            thread {
                searchEscola()
            }
        })
    }

    /****************************
     *      LOCALIZATION        *
     ****************************/

    //Obtém a localização atual
    private fun getCurrentLocation() {
        val localization:LocalizationService = LocalizationService(this@LocalizarEscolaActivity)
        localization.callCurrentLocation({location ->
            this.runOnUiThread {
                this.txtView.text = "Latitude: " + location?.getLatitude() + "\n" +
                        "Longitude: " + location?.getLongitude();
            }
        })
    }

    //Obtém a última localização do GPS, caso não tenha obtém a atual
    private fun getLastLocation() {
        val localization:LocalizationService = LocalizationService(this@LocalizarEscolaActivity)
        localization.callLastKnownLocation({location ->
            thread {
                getEscolaByLocale(location?.longitude, location?.latitude)
                this.runOnUiThread {
                    this.txtView.text = "Latitude: " + location?.latitude + "\n" +
                            "Longitude: " + location?.longitude;
                }
            }
        })
    }

    //Procura no banco escolas com longitude e latitude parecidas com a atual
    private fun getEscolaByLocale(x:Double?, y:Double?) {
        if(x == null || y == null)
            return

        val escolaGeometry:List<EscolaGeometry>? = AppDatabase.getDatabase(this)?.escolaDao()?.findGeometryBetweenXY(x, y)
        var escolasMap:HashMap<Long,Escola> = HashMap()

        escolaGeometry?.forEach { geometry ->
            if(!escolasMap.containsKey(geometry.escolaId)) {
                val escola:Escola? = EscolaRepository.getEscolaById(geometry.escolaId, this)
                if(escola != null)
                    escolasMap.put(geometry.escolaId, escola)
            }
        }
        runOnUiThread({
            initEscolaView(ArrayList(escolasMap.values))
        })

    }

    /****************************
     *      PERMISSÕES          *
     ****************************/

    //TODO Melhorar isso.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LocalizationService.REQUEST_PERMISSIONS_LAST_LOCATION_REQUEST_CODE) {
            if (grantResults.size <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(LocalizationService.TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            }
        }

        if (requestCode == LocalizationService.REQUEST_PERMISSIONS_CURRENT_LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            }
        }
    }
}
