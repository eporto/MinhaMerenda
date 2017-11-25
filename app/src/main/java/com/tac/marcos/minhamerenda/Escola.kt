package com.example.marcos.okhttptest

import com.tac.marcos.minhamerenda.Avaliacao
import java.io.Serializable

/**
 * Created by Marcos on 12/11/2017.
 */

class Escola : Serializable{
    //add ID
    private var escolaID: Int? = null;
    private var escolaNome: String? = null
    private var longitude: String? = null
    private var latitude: String? = null

    constructor(escolaNome: String, longitude: String, latitude: String, escolaID: Int) {
        this.escolaID = escolaID
        this.escolaNome = escolaNome
        this.longitude = longitude
        this.latitude = latitude
    }

    constructor()

    //FUNCTIONS
    fun getAvaliacao(a: ArrayList<Avaliacao>): Float?{
        var avaliacao: Float? = 0F
        var qtd = 0
        for (i in a){
            if(i.getEscola()!!.getEscolaId() == this.escolaID){
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

    fun setEscolaID(s : Int) {
        this.escolaID = s
    }

    fun getEscolaNome(): String? {
        return this.escolaNome
    }

    fun setEscolaNome(s : String) {
        this.escolaNome = s
    }

    fun getLongitude(): String? {
        return this.longitude
    }

    fun setLongitude(s : String) {
        this.longitude = s
    }

    fun getLatitude(): String? {
        return this.latitude
    }

    fun setLatitude(s : String) {
        this.latitude = s
    }




}
