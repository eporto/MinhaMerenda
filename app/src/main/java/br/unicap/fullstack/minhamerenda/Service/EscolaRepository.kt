package com.example.marcos.okhttptest

import android.content.Context
import br.unicap.fullstack.minhamerenda.Data.AppDatabase
import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Data.EscolaGeometry
import br.unicap.fullstack.minhamerenda.Helper.JsonParser
import br.unicap.fullstack.minhamerenda.R
import br.unicap.fullstack.minhamerenda.Service.HttpService
import okhttp3.Response
import org.json.JSONArray

class EscolaRepository() {

    companion object {
        fun getEscolaFromServer(context: Context) : List<Escola>{
            val httpService:HttpService = HttpService()
            val response:Response = httpService.doGet(context.getString(R.string.endpoint_list_escola))

            if(response.code() != 200) {
                return emptyList()
            } else {
                val responseBody = response.body()?.string() //.body é consumido uma vez só
                if(responseBody!= null) {
                    return JsonParser.parseToListEscola(JSONArray(responseBody))
                } else
                    return emptyList()
            }
        }

        fun getEscolaGeometryFromServer(context: Context) : List<EscolaGeometry>{
            val httpService:HttpService = HttpService()
            val response:Response = httpService.doGet(context.getString(R.string.endpoint_list_geometry))

            if(response.code() != 200) {
                return emptyList()
            } else {
                val responseBody = response.body()?.string() //.body é consumido uma vez só
                if(responseBody!= null) {
                    return JsonParser.parseToListGeometry(JSONArray(responseBody))
                } else
                    return emptyList()
            }
        }

        fun listAll(context:Context) : List<Escola>? {
            return AppDatabase.getDatabase(context)?.escolaDao()?.findAll()
        }
        fun getEscolaByName(nome:String, context:Context) : List<Escola>? {
            return AppDatabase.getDatabase(context)?.escolaDao()?.findEscolaByNome(nome + "%")
        }

        fun getEscolaById(id:Long, context:Context) : Escola? {
            return AppDatabase.getDatabase(context)?.escolaDao()?.findEscolaById(id)
        }

        fun getEscolaByCod(cod:Int, context:Context) : List<Escola>? {
            return AppDatabase.getDatabase(context)?.escolaDao()?.findEscolaByCod(cod)
        }


        fun insertEscolas(escolaList:List<Escola>, context:Context) : LongArray? = AppDatabase.getDatabase(context)?.escolaDao()?.addEscola(escolaList)
        fun insertGeometries(geometryList:List<EscolaGeometry>, context: Context) : LongArray? = AppDatabase.getDatabase(context)?.escolaDao()?.addEscolaGeometry(geometryList)

    }
}
