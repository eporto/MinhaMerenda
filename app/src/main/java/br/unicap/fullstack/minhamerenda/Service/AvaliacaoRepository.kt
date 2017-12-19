package br.unicap.fullstack.minhamerenda.Service

import android.content.Context
import android.util.Log
import br.unicap.fullstack.minhamerenda.Model.Avaliacao
import br.unicap.fullstack.minhamerenda.Helper.JsonParser
import br.unicap.fullstack.minhamerenda.Helper.SettingsHelper
import br.unicap.fullstack.minhamerenda.R
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class AvaliacaoRepository() {

    companion object {

        fun listAll(appKey:String, appToken:String, context: Context): List<Avaliacao> {
            val appKeyString: String = context.getString(R.string.prefs_appkey)
            val appTokenString: String = context.getString(R.string.prefs_apptoken)
            val headers: Map<String, String> = hashMapOf(appKeyString to appKey, appTokenString to appToken)

            val client: HttpService = HttpService()
            val response: Response = client.doGet(context.getString(R.string.endpoint_list_avaliacao), headers)

            if(response.code() != 200) {
                return emptyList()
            } else {
                val responseBody = response.body()?.string() //.body é consumido uma vez só
                if(responseBody!= null) {
                    return JsonParser.parseToListAvaliacao(JSONArray(responseBody))
                } else
                    return emptyList()
            }
        }

        fun listAvaliacaoByEscola(escolaId:String, context: Context): List<Avaliacao> {
            val headers: Map<String, String> = SettingsHelper.getCredentialsAsMap(context)

            val client: HttpService = HttpService()
            //list/avaliacao/{id}
            val response: Response = client.doGet(context.getString(R.string.endpoint_list_avaliacao) + "/" + escolaId, headers)

            if(response.code() != 200) {
                return emptyList()
            } else {
                val responseBody = response.body()?.string() //.body é consumido uma vez só
                if(responseBody!= null) {
                    return JsonParser.parseToListAvaliacao(JSONArray(responseBody))
                } else
                    return emptyList()
            }
        }

        fun postAvaliacaoToServer(newAvaliacao:JSONObject, filePath:String, context: Context) : Boolean {
            val credentials:Map<String, String> = SettingsHelper.getCredentialsAsMap(context)
            val file:File = File(filePath)
            val avaliacaoId:Long? = postAvaliacao(context.getString(R.string.endpoint_create_avaliacao),newAvaliacao, credentials)

            if(avaliacaoId != null) {
                return postFoto(context.getString(R.string.endpoint_create_avaliacao_foto), avaliacaoId.toInt(), filePath, credentials)
            }
            else
                return false
        }

        private fun postAvaliacao(requestUrl:String, avaliacao:JSONObject, credentials:Map<String, String>) : Long? {
            val httpService: HttpService = HttpService()
            val response: Response = httpService.doPost(
                    requestUrl,
                    avaliacao.toString(),
                    credentials
            )
            if (response.code() != 200) {
                Log.i("postavaliacao", response.message())
                return null
            }
            else {
                val responseBody:String? = response.body()?.string()
                if(responseBody != null) {
                    try {
                        return responseBody.toLong()
                    } catch (e:Exception) {
                        return null
                    }
                } else
                    return null
            }
        }

        private fun postFoto(requestUrl:String, avaliacaoId:Int, filePath:String, credentials: Map<String, String>) : Boolean {
            val file:File = File(filePath)
            val httpService: HttpService = HttpService()
            val response: Response = httpService.doPostMultipart(
                    requestUrl,
                    file,
                    credentials,
                    avaliacaoId.toString()
            )
            if (response.code() != 200) {
                Log.i("postfoto", response.message())
                return false
            }
            else
               return true
        }
    }
}
