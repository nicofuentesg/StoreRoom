package com.example.stores

import android.widget.Toast
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Tienda::class), version = 2)
abstract class TiendaDataBase: RoomDatabase() {
    abstract fun tiendaDao(): TiendaDAO
}