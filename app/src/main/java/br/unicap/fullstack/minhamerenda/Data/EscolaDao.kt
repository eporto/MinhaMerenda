package br.unicap.fullstack.minhamerenda.Data

import android.arch.persistence.room.*

/**
 * Created by shido on 03/12/2017.
 */
@Dao
interface EscolaDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEscola(escola: Escola);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEscola(escola: List<Escola>): LongArray

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEscolaGeometry(geometry: EscolaGeometry);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addEscolaGeometry(geometry: List<EscolaGeometry>) : LongArray

    @Query("SELECT * FROM escolas")
    fun findAll(): List<Escola>

    @Query("select * from escolas where lower(escola_nome) like lower(:nome)")
    fun findEscolaByNome(nome:String): List<Escola>

    @Query("select * from escolas where escola_codigo = :codigo")
    fun findEscolaByCod(codigo:Int): List<Escola>

    @Query("select * from escolas where id = :id")
    fun findEscolaById(id:Long): Escola

    @Query("SELECT * FROM escolasgeometry")
    fun findAllGeometry(): List<EscolaGeometry>

    @Query("SELECT * FROM escolasgeometry where id_escola = :cod")
    fun findGeometryByEscolaId(cod:Long): List<EscolaGeometry>

    @Query("select * from escolas where escola_codigo = :cod")
    fun findEscolaByEscolaCodigo(cod: Int): List<Escola>

    //Teste
    @Query("select * from escolasgeometry where CAST(x as decimal) between (:in_x - :offset)  and (:in_x + :offset) and CAST(y as decimal) between (:in_y - :offset)  and (:in_y + :offset)")
    fun findGeometryBetweenXY(in_x: Double, in_y:Double, offset:Double = EscolaGeometry.DISTANCE_OFFSET): List<EscolaGeometry>


    /* @Query("SELECT escola_nome, x, y from escolas, escolasgeometry where escolas.escola_codigo = escolasgeometry.escola_codigo")
     fun findEscolaAndCoords(): List<EscolaAndCoords>*/

    @Update
    fun updateUser(user: Escola);

    @Delete
    fun deleteUser(user: Escola);
}
