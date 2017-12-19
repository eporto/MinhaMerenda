package br.unicap.fullstack.minhamerenda.Model

import java.io.Serializable

data class Usuario(
        val id:Long,
        val tipo:String
) : Serializable {
    override fun toString(): String = this.tipo
}