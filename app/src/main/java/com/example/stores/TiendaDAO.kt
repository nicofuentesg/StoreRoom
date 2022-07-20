package com.example.stores

import androidx.room.*

@Dao
interface TiendaDAO {
    @Query("SELECT * FROM TiendaEntity")
    fun getAllStores(): MutableList<Tienda>

    @Query("SELECT *FROM TiendaEntity where id = :id")
    fun getStorebyId( id:Long): Tienda

    @Insert
    fun addStore(tienda: Tienda):Long

    @Update
    fun updateStore(tienda: Tienda)

    @Delete
    fun deleteStore(tienda: Tienda)
}