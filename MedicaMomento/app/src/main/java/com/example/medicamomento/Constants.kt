package com.example.medicamomento

import android.provider.BaseColumns

object Constants {
    // Table contents are grouped together in an anonymous object.
    object medicinas : BaseColumns {
        const val TABLE_NAME = "Mis_Medicamentos"
        const val COLUMN_MEDICAMENTO = "Medicamento"
        const val COLUMN_DOSIS = "Dosis"
        const val COLUMN_FECHA = "Finalizacion"
        const val COLUMN_HORARIO = "HORAS"
    }
    object perfil : BaseColumns {
        const val TABLE_NAME = "PERFIL"
        const val COLUMN_NOMBRE = "NOMBRE"
        const val COLUMN_EDAD = "EDAD"
        const val COLUMN_SANGRE = "T_SANGRE"
        const val COLUMN_ENFERMEDADES = "ENFERMEDADES"
        const val COLUMN_ALERGIAS = "ALERGIAS"
        const val COLUMN_SERVICIO = "S_MEDICO"
    }
}