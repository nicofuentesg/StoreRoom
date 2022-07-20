package com.example.stores

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TiendaEntity")
data class Tienda(@PrimaryKey(autoGenerate = true)
                  var id:Long = 0,
                  var nombreDeLaTienda:String,
                  var telefono:String = "",
                  var sitioWeb:String = "",
                  var fotoUrl:String,
                  var favorita:Boolean = false){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tienda

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}


