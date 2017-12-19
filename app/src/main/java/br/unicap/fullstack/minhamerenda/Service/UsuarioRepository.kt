package br.unicap.fullstack.minhamerenda.Service

import android.content.Context
import br.unicap.fullstack.minhamerenda.Helper.JsonParser
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.Model.Usuario
import br.unicap.fullstack.minhamerenda.R
import okhttp3.Response
import org.json.JSONArray

/**
 * Created by shido on 08/12/2017.
 */
class UsuarioRepository() {
    companion object {
        //TODO por enquanto preciso disso
        fun getUsuarioById(id:Int): String {
            return when(id) {
                1 -> "Aluno"
                2 -> "Escola"
                3 -> "Fabricante"
                4 -> "Transportadora"
                else -> "Usuário Inválido"
            }
        }

        fun getUsuarioFromServer(context: Context): List<Usuario> {
            val credentials:Map<String,String> = SettingsHelper.getCredentialsAsMap(context)
            val httpService:HttpService = HttpService()
            val response:Response = httpService.doGet(context.getString(R.string.endpoint_list_usuario),credentials)

            if(response.code() != 200) {
                return emptyList()
            } else {
                val responseBody = response.body()?.string() //.body é consumido uma vez só
                if(responseBody!= null) {
                    return JsonParser.parseToListUsuario(JSONArray(responseBody))
                } else
                    return emptyList()
            }

        }
    }
}