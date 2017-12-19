package br.unicap.fullstack.minhamerenda.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by shido on 03/12/2017.
 */

@Entity(tableName = "escolas")
data class Escola(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "escola_codigo") val escolaCodigo: Int,
        @ColumnInfo(name = "escola_nome") val escolaNome: String,
        val endereco: String,
        @ColumnInfo(name = "mec_codigo") val mecCodigo: Int

) : Serializable {
    override fun toString(): String = this.escolaNome


}