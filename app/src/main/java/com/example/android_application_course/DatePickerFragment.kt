package com.example.android_application_course

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

private const val YEAR = "year"
private const val MONTH = "month"
private const val DAY = "day"

interface OnDateSetListener {
    fun onDateSet(year: Int, month: Int, day: Int)
}

fun padDate(date: Int): String {
    return date.toString().padStart(2, '0')
}

fun dateString(year: Int, month: Int, day: Int): String {
    return "${padDate(day)}/${padDate(month + 1)}/$year"
}

class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private var listener: OnDateSetListener? = null
    private var year: Int? = null
    private var month: Int? = null
    private var day: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val c = Calendar.getInstance()
        arguments?.let {
            year = it.getInt(YEAR, c.get(Calendar.YEAR))
            month = it.getInt(MONTH, c.get(Calendar.MONTH))
            day = it.getInt(DAY, c.get(Calendar.DAY_OF_MONTH))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return DatePickerDialog(requireContext(), this, year as Int, month as Int, day as Int)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        listener?.onDateSet(year, month, day)
    }

    fun setOnDateSet(listener: OnDateSetListener): DatePickerFragment {
        this.listener = listener
        return this
    }

    companion object {
        fun newInstance(year: Int?, month: Int?, day: Int?) =
            DatePickerFragment().apply {
                arguments = Bundle().apply {
                    if (year != null) putInt(YEAR, year)
                    if (month != null) putInt(MONTH, month)
                    if (day != null) putInt(DAY, day)
                }
            }
    }
}