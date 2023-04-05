package com.myapplication.others

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import com.myapplication.base.BaseActivity
import java.util.*

object DateHelper {

    fun openDatePicker(mCtx: BaseActivity, callBack: CallBack<Calendar>) {
        val c = Calendar.getInstance()
        c.time = Date()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            mCtx,
            DatePickerDialog.OnDateSetListener { view, year_, monthOfYear, dayOfMonth ->
                c.set(Calendar.YEAR, year_)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                c.set(Calendar.MONTH, monthOfYear)
                openTimePicker(mCtx, c, callBack)

            }, year, month, day
        )
        dpd.getDatePicker().setMinDate(Calendar.getInstance().timeInMillis);
        dpd.setCancelable(false)
        dpd.show()

    }

    fun onClick(dialog: DialogInterface?, which: Int) {
        if (which == DialogInterface.BUTTON_POSITIVE) {
            Toaster.shortToast("")
        }

    }

    internal fun openTimePicker(
        mCtx: BaseActivity,
        cal: Calendar,
        callBack: CallBack<Calendar>
    ) {

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            callBack.onSuccess(cal)
        }

        TimePickerDialog(
            mCtx,
            timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE),
            true
        ).show()

    }

}