package com.example.marcos.okhttptest

import com.tac.marcos.minhamerenda.Avaliacao
import java.io.Serializable

/**
 * Created by Marcos on 12/11/2017.
 */

class Escola : Serializable{
    //add ID
    private var escolaID: Int? = null;
    private var escolaCodigo: Int? = null;
    private var escolaNome: String? = null
    private var endereco: String? = null
    private var mecCodigo: String? = null

    constructor(escolaNome: String, longitude: String, latitude: String, escolaID: Int) {
        this.escolaCodigo = escolaID
        this.escolaNome = escolaNome
        this.endereco = longitude
        this.mecCodigo = latitude
    }

    constructor()

    //FUNCTIONS
    fun getAvaliacao(a: ArrayList<Avaliacao>): Float?{
        var avaliacao: Float? = 0F
        var qtd = 0
        for (i in a){
            if(i.getEscola()!!.getEscolaCodigo() == this.escolaCodigo){
                avaliacao = avaliacao!! + i.getPontuacao()!!
                qtd ++
            }
        }
        avaliacao = if(qtd == 0) null
        else avaliacao!! / qtd
        return avaliacao
    }

    //GETTERS AND SETTERS
    fun getEscolaId(): Int? {
        return this.escolaID
    }

    fun setEscolaId(s : Int) {
        this.escolaID = s
    }

    fun getEscolaCodigo(): Int? {
        return this.escolaCodigo
    }

    fun setEscolaCodigo(s : Int) {
        this.escolaCodigo = s
    }

    fun getEscolaNome(): String? {
        return this.escolaNome
    }

    fun setEscolaNome(s : String) {
        this.escolaNome = s
    }

    fun getEndereco(): String? {
        return this.endereco
    }

    fun setEndereco(s : String) {
        this.endereco = s
    }

    fun getMecCodigoe(): String? {
        return this.mecCodigo
    }

    fun setMecCodigo(s : String) {
        this.mecCodigo = s
    }




}
