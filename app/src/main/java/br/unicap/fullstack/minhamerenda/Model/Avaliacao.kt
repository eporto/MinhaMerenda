package br.unicap.fullstack.minhamerenda.Model

data class Avaliacao(
        val id: Long,
        val pontuacao:Int,
        val escola:br.unicap.fullstack.minhamerenda.Data.Escola,
        val foto:String
)