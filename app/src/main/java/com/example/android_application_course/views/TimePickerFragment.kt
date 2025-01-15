package com.example.android_application_course.views

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

private const val HOUR = "hour"
private const val MINUTE = "minute"

interface OnTimeSetListener {
    fun onTimeSet(hourOfDay: Int, minute: Int)
}

fun timeString(hourOfDay: Int, minute: Int): String {
    var hour = hourOfDay
    var suffix = "AM"
    if (hourOfDay > 12) {
        hour -= 12
        suffix = "PM"
    }
    return "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')} $suffix"
}

class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {
    private var listener: OnTimeSetListener? = null
    private var hour: Int? = null
    private var minute: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val c = Calendar.getInstance()
        arguments?.let {
            hour = it.getInt(HOUR, c.get(Calendar.HOUR_OF_DAY))
            minute = it.getInt(MINUTE, c.get(Calendar.MINUTE))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(activity, this, hour as Int, minute as Int, false)
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        listener?.onTimeSet(hourOfDay, minute)
    }

    fun setOnTimeSet(listener: OnTimeSetListener): TimePickerFragment {
        this.listener = listener
        return this
    }

    companion object {
        fun newInstance(hour: Int?, minute: Int?) =
            TimePickerFragment().apply {
                arguments = Bundle().apply {
                    if (hour != null) putInt(HOUR, hour)
                    if (minute != null) putInt(MINUTE, minute)
                }
            }
    }
}