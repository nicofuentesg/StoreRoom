package com.example.stores

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class TiendaAplicacion: Application() {
    companion object{
        lateinit var database: TiendaDataBase
    }

    override fun onCreate() {
        super.onCreate()

        var MIGRATION_1_2 = object : Migration(1, 2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE TiendaEntity ADD COLUMN fotoUrl TEXT NOT NULL DEFAULT ''")
            }
        }

        database = Room.databaseBuilder(this,
            TiendaDataBase::class.java,
            "TiendaDatabase")
            .addMigrations(MIGRATION_1_2)
            .build()


    }
}