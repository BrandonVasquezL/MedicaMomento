package com.example.medicamomento

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DBhelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase ) {
        db.execSQL(SQL_CREATE_MEDICAMENTOS)
        db.execSQL(SQL_CREATE_PROFILE)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_DELETE_PROFILE)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
    private  val SQL_CREATE_MEDICAMENTOS =
        "CREATE TABLE ${Constants.medicinas.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Constants.medicinas.COLUMN_MEDICAMENTO} TEXT," +
                "${Constants.medicinas.COLUMN_DOSIS} TEXT," +
                "${Constants.medicinas.COLUMN_FECHA} DATE," +
                "${Constants.medicinas.COLUMN_HORARIO} TIME)"


    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Constants.medicinas.TABLE_NAME}"

    private  val SQL_CREATE_PROFILE =
        "CREATE TABLE ${Constants.perfil.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Constants.perfil.COLUMN_NOMBRE} TEXT," +
                "${Constants.perfil.COLUMN_EDAD} TEXT," +
                "${Constants.perfil.COLUMN_SANGRE} TEXT," +
                "${Constants.perfil.COLUMN_ENFERMEDADES} TEXT," +
                "${Constants.perfil.COLUMN_ALERGIAS} TEXT," +
                "${Constants.perfil.COLUMN_SERVICIO} TEXT)"


    private val SQL_DELETE_PROFILE = "DROP TABLE IF EXISTS ${Constants.perfil.TABLE_NAME}"
}