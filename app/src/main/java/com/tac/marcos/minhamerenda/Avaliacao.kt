package com.tac.marcos.minhamerenda

import com.example.marcos.okhttptest.Escola
import java.io.Serializable

/**
 * Created by Marcos on 19/11/2017.
 */
class Avaliacao : Serializable{
    private var avaliacaoID: Int? = null
    private var pontuacao: Int? = null
    private var escola: Escola? = null
    private var foto: String? = null
    private var usuario: Usuario? = null

    constructor(avaliacaoID: Int?, pontuacao: Int?, escola: Escola?, foto: String?, usuario: Usuario?) {
        this.avaliacaoID = avaliacaoID
        this.pontuacao = pontuacao
        this.escola = escola
        this.foto = foto
        this.usuario = usuario
    }

    constructor()

    //FUNCTIONS


    //GETTERS AND SETTERS
    fun getAvaliacaoID(): Int? {
        return this.avaliacaoID
    }

    fun setAvaliacaoID(s : Int) {
        this.avaliacaoID = s
    }

    fun getPontuacao(): Int? {
        return this.pontuacao
    }

    fun setPontuacao(s : Int) {
        this.pontuacao = s
    }

    fun getEscola(): Escola? {
        return this.escola
    }

    fun setEscola(s : Escola) {
        this.escola = s
    }

    fun getFoto(): String? {
        return this.foto
    }

    fun setFoto(s : String) {
        this.foto = s
    }

    fun getUsuario(): Usuario? {
        return this.usuario
    }

    fun setUsuario(s : Usuario) {
        this.usuario = s
    }
}