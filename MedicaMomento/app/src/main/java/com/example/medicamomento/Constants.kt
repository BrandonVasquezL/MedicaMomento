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
}