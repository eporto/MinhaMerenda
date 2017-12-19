package br.unicap.fullstack.minhamerenda.Data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by shido on 03/12/2017.
 */
@Entity(tableName = "escolasgeometry")
data class EscolaGeometry(
        @PrimaryKey val id: Long,
        @ColumnInfo(name = "id_escola") val escolaId: Long,
        @ColumnInfo(name = "x") val x: String,
        @ColumnInfo(name = "y") val y: String
) {
    companion object {
        val DISTANCE_OFFSET : Double =  0.003000000000000
    }
}
/*
x: 5, 10, 15, 20
y: 4, 8 , 10, 12

offset:2
a: 12 -> 10 | 14
b: 3 -> 1 | 5

returned x: 10
returned y: 4
        */