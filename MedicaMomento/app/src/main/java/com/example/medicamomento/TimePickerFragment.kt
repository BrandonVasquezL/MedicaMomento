package com.example.medicamomento

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TimePickerFragment(val listener: (String)-> Unit):DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val dialog = TimePickerDialog(activity as Context,R.style.datePickerTheme, this,hour,minute,true)
        return dialog
    }

    override fun onTimeSet(view: TimePicker?, hourOfDat: Int, minute: Int) {
        listener("$hourOfDat:$minute")
    }

}