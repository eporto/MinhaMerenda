package br.unicap.fullstack.minhamerenda.Helper

import br.unicap.fullstack.minhamerenda.Data.Escola
import br.unicap.fullstack.minhamerenda.Data.EscolaGeometry
import br.unicap.fullstack.minhamerenda.Model.Avaliacao
import br.unicap.fullstack.minhamerenda.Model.Usuario
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by shido on 07/12/2017.
 */
class JsonParser() {
    companion object {

        fun parseToListAvaliacao(data: JSONArray) : List<Avaliacao> {
            var avaliacaoList:ArrayList<Avaliacao> = ArrayList<Avaliacao>()

            for (i in 0 until data.length()) {
                val avaliacao:Avaliacao = JsonParser.parseToAvaliacao(data.getJSONObject(i))

                avaliacaoList.add(avaliacao)
            }

            return avaliacaoList
        }

        fun parseToListEscola(data:JSONArray): List<Escola> {
            var escolaList:ArrayList<Escola> = ArrayList<Escola>()

            for (i in 0 until data.length()) {
                val escolaJson : JSONObject = data.getJSONObject(i)
                val escola:Escola = JsonParser.parseToEscola(escolaJson)

                escolaList.add(escola)
            }

            return escolaList
        }

        fun parseToAvaliacao(data:JSONObject): Avaliacao {
            val escolaJson:JSONObject = data.getJSONObject("escola")
            val escola:Escola = JsonParser.parseToEscola(escolaJson)

            val avaliacao:Avaliacao = Avaliacao(
                    data.getLong("id"),
                    data.getInt("pontuacao"),
                    escola,
                    data.getString("foto")
            )

            return avaliacao
        }

        fun parseToListGeometry(data:JSONArray): List<EscolaGeometry> {
            var geometryList:ArrayList<EscolaGeometry> = ArrayList<EscolaGeometry>()

            for (i in 0 until data.length()) {
                val geometryJson:JSONObject = data.getJSONObject(i)
                val geometry:EscolaGeometry = JsonParser.parseToGeometry(geometryJson)

                geometryList.add(geometry)
            }

            return geometryList
        }

        fun parseToListUsuario(data:JSONArray): List<Usuario> {
            var usuarioList:ArrayList<Usuario> = ArrayList<Usuario>()

            for (i in 0 until data.length()) {
                val usuarioJson:JSONObject = data.getJSONObject(i)
                val usuario:Usuario= JsonParser.parseToUsuario(usuarioJson)

                usuarioList.add(usuario)
            }
            return usuarioList
        }

        fun parseToUsuario(data:JSONObject): Usuario {
            val usuario:Usuario= Usuario(
                    data.getLong("id"),
                    data.getString("tipo")
            )
            return usuario
        }

        fun parseToGeometry(data:JSONObject): EscolaGeometry {
            val geometry:EscolaGeometry = EscolaGeometry(
                    data.getLong("id"),
                    data.getLong("escolaId"),
                    data.getString("x"),
                    data.getString("y")
            )

            return geometry
        }

        fun parseToEscola(data:JSONObject): Escola {
            val escola:Escola = Escola(
                    data.getLong("id"),
                    data.getInt("escolaCodigo"),
                    data.getString("escolaNome"),
                    data.getString("endereco"),
                    data.getInt("mecCodigo")
            )

            return escola
        }
    }
}