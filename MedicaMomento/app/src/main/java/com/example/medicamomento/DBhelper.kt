package com.example.medicamomento

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

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
                "${Constants.medicinas.COLUMN_HORARIO} TIME," +
                "${Constants.medicinas.COLUMN_IMAGEN} BLOB)"


    private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${Constants.medicinas.TABLE_NAME}"

    private  val SQL_CREATE_PROFILE =
        "CREATE TABLE ${Constants.perfil.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${Constants.perfil.COLUMN_NOMBRE} TEXT," +
                "${Constants.perfil.COLUMN_EDAD} INT," +
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
        val horario: String,
        val fecha: String,
        val imagen: ByteArray
    )
    fun getMedicamentos(): List<Medicamento> {
        val db = this.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            Constants.medicinas.COLUMN_MEDICAMENTO,
            Constants.medicinas.COLUMN_DOSIS,
            Constants.medicinas.COLUMN_HORARIO,
            Constants.medicinas.COLUMN_FECHA,
            Constants.medicinas.COLUMN_IMAGEN
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
                val fecha = getString(getColumnIndexOrThrow(Constants.medicinas.COLUMN_FECHA))
                val imagen = getBlob(getColumnIndexOrThrow(Constants.medicinas.COLUMN_IMAGEN))
                medicamentos.add(Medicamento(id, nombre, dosis, horario, fecha, imagen))
            }
        }
        cursor.close()
        return medicamentos
    }

    data class Perfil(
        val id: Int,
        val nombre: String,
        val edad: Int,
        val sangre: String,
        val enfermedades: String,
        val alergias: String,
        val servicio: String
    )
    fun getPerfil(): MutableList<Perfil> {
        val db = this.readableDatabase
        val projection = arrayOf(
        BaseColumns._ID,
        Constants.perfil.COLUMN_NOMBRE,
        Constants.perfil.COLUMN_EDAD,
        Constants.perfil.COLUMN_SANGRE,
        Constants.perfil.COLUMN_ENFERMEDADES,
        Constants.perfil.COLUMN_ALERGIAS,
        Constants.perfil.COLUMN_SERVICIO
        )
        val cursor = db.query(
            Constants.perfil.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            null
        )
        val usuario = mutableListOf<Perfil>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val nombre = getString(getColumnIndexOrThrow(Constants.perfil.COLUMN_NOMBRE))
                val edad = getInt(getColumnIndexOrThrow(Constants.perfil.COLUMN_EDAD))
                val sangre = getString(getColumnIndexOrThrow(Constants.perfil.COLUMN_SANGRE))
                val enfermedades = getString(getColumnIndexOrThrow(Constants.perfil.COLUMN_ENFERMEDADES))
                val alergias = getString(getColumnIndexOrThrow(Constants.perfil.COLUMN_ALERGIAS))
                val servicio = getString(getColumnIndexOrThrow(Constants.perfil.COLUMN_SERVICIO))
                usuario.add(Perfil(id, nombre, edad, sangre, enfermedades, alergias, servicio))
            }
        }
        cursor.close()
        return usuario
    }
    fun actualizarPerfil(perfil: Perfil) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(Constants.perfil.COLUMN_NOMBRE, perfil.nombre)
            put(Constants.perfil.COLUMN_EDAD, perfil.edad)
            put(Constants.perfil.COLUMN_SANGRE, perfil.sangre)
            put(Constants.perfil.COLUMN_ENFERMEDADES, perfil.enfermedades)
            put(Constants.perfil.COLUMN_ALERGIAS, perfil.alergias)
            put(Constants.perfil.COLUMN_SERVICIO, perfil.servicio)
        }

        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(perfil.id.toString())

        db.update(Constants.perfil.TABLE_NAME, values, selection, selectionArgs)
    }

}