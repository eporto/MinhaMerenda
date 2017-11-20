package com.tac.marcos.minhamerenda

import java.io.Serializable

/**
 * Created by Marcos on 19/11/2017.
 */
class Usuario: Serializable{
    private var usuarioID: Int? = null
    private var tipo: String? = null

    constructor(usuarioID: Int?, tipo: String?) {
        this.usuarioID = usuarioID
        this.tipo = tipo
    }

    constructor()
    //FUNCTIONS

    //GETTERS AND SETTERS
    fun getUsuarioID(): Int? {
        return this.usuarioID
    }

    fun setUsuarioID(s : Int) {
        this.usuarioID = s
    }

    fun getTipo(): String? {
        return this.tipo
    }

    fun setTipo(s : String) {
        this.tipo = s
    }
}