package com.example.medicamomento

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory.Companion.instance

class DBhelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase ) {
        db.execSQL(SQL_CREATE_MEDICAMENTOS)
        db.execSQL(SQL_CREATE_PROFILE)
        db.execSQL(SQL_CREATE_COMENTARIO)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES)
        db.execSQL(SQL_DELETE_PROFILES)
        db.execSQL(SQL_DELETE_COMENTARIOS)
        onCreate(db)

    }


    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"

        private var instance: DBhelper? = null

        fun getInstance(context: Context): DBhelper {
            return instance ?: synchronized(this) {
                instance ?: DBhelper(context.applicationContext).also { instance = it }
            }
        }
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


    private val SQL_DELETE_PROFILES = "DROP TABLE IF EXISTS ${Constants.perfil.TABLE_NAME}"

    private  val SQL_CREATE_COMENTARIO =
        "CREATE TABLE ${Constants.comentarios.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Constants.comentarios.COLUMN_COMENT} TEXT)"


    private val SQL_DELETE_COMENTARIOS= "DROP TABLE IF EXISTS ${Constants.comentarios.TABLE_NAME}"

    data class Medicamento(
        val id: Int,
        val nombre: String,
        val dosis: String,
        val horario: String
    )
    fun getMedicamentos(): List<Medicamento> {
        val db = this.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            Constants.medicinas.COLUMN_MEDICAMENTO,
            Constants.medicinas.COLUMN_DOSIS,
            Constants.medicinas.COLUMN_HORARIO
        )
        val cursor = db.query(
            Constants.medicinas.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val medicamentos = mutableListOf<Medicamento>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val nombre = getString(getColumnIndexOrThrow(Constants.medicinas.COLUMN_MEDICAMENTO))
                val dosis = getString(getColumnIndexOrThrow(Constants.medicinas.COLUMN_DOSIS))
                val horario = getString(getColumnIndexOrThrow(Constants.medicinas.COLUMN_HORARIO))
                medicamentos.add(Medicamento(id, nombre, dosis, horario))
            }
        }
        cursor.close()
        return medicamentos
    }
}