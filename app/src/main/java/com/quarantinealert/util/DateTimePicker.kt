package com.quarantinealert.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.*

class DateTimePicker(val context: Context, var pickTime:Boolean = false, var calendar: Calendar = Calendar.getInstance(),
                     var callback: (it : DateTimePicker) -> Unit) {

    companion object{
        @JvmStatic
        fun getFormat(format : String) : String{
            when(format){
                "d" -> return "dd/MM/yyyy"
                "t" -> return "HH:mm"
                "dt" -> return "MMM dd, yyyy h:mm a"
            }
            return "dd/MM/yyyy"
        }
    }

    fun show(){
        val startYear = calendar.get(Calendar.YEAR)
        val startMonth = calendar.get(Calendar.MONTH)
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = calendar.get(Calendar.MINUTE)

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            if(pickTime) {
                TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    calendar.set(year, month, day, hour, minute)
                    callback(this)
                }, startHour, startMinute, true).show()
            } else {
                calendar.set(year,month,day)
                callback(this)
            }
        }, startYear, startMonth, startDay).show()
    }

    fun showTime(){
        val startYear = calendar.get(Calendar.YEAR)
        val startMonth = calendar.get(Calendar.MONTH)
        val startDay = calendar.get(Calendar.DAY_OF_MONTH)
        val startHour = calendar.get(Calendar.HOUR_OF_DAY)
        val startMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(startYear, startMonth, startDay, hour, minute)
            callback(this)
        }, startHour, startMinute, true).show()
    }
}