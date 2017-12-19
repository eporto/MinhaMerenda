package br.unicap.fullstack.minhamerenda.Data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context


@Database(entities = arrayOf(Escola::class, EscolaGeometry::class), version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun escolaDao():EscolaDao

    companion object {
        private val DB_NAME = "escolas_db";
        private var dbInstance: AppDatabase? = null;

        fun getDatabase(context: Context): AppDatabase? {
            if(dbInstance == null)
                dbInstance = Room.databaseBuilder<AppDatabase>(context.applicationContext, AppDatabase::class.java, DB_NAME).build();
                //dbInstance = Room.databaseBuilder<AppDatabase>(context.applicationContext, AppDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build(); //Debug

            return dbInstance;
        }
    }
}