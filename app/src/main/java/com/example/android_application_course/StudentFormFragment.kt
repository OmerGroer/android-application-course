package com.example.android_application_course

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android_application_course.model.Model
import com.example.android_application_course.model.Student

private const val STUDENT_POSITION = "studentPosition"

interface OnSaveListener {
    fun onSave(view: View)
}

class StudentFormFragment : Fragment(), OnTimeSetListener, OnDateSetListener {
    private var onSaveListener: OnSaveListener? = null

    var saveButton: Button? = null
    var deleteButton: Button? = null
    var cancelButton: Button? = null
    var nameField: EditText? = null
    var idField: EditText? = null
    var phoneField: EditText? = null
    var addressField: EditText? = null
    var checkBoxField: CheckBox? = null
    var birthDate: EditText? = null
    var birthTime: EditText? = null

    private var studentPosition: Int? = null
    private var birthHour: Int? = null
    private var birthMinute: Int? = null
    private var birthDay: Int? = null
    private var birthMonth: Int? = null
    private var birthYear: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            studentPosition = it.getInt(STUDENT_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_form, container, false)

        setup(view)

        if (studentPosition != null) {
            initiateForm()
        } else {
            deleteButton?.visibility = View.GONE
        }

        saveButton?.setOnClickListener(::onSaveButton)
        cancelButton?.setOnClickListener(::onCancelButton)
        deleteButton?.setOnClickListener(::onDeleteButton)

        birthTime?.setOnClickListener {
            TimePickerFragment.newInstance(birthHour, birthMinute).setOnTimeSet(this).show(getChildFragmentManager(), "timePicker")
        }
        birthDate?.setOnClickListener {
            DatePickerFragment.newInstance(birthYear, birthMonth, birthDay).setOnDateSet(this).show(getChildFragmentManager(), "datePicker")
        }

        return view
    }

    fun setOnSave(listener: OnSaveListener): StudentFormFragment {
        this.onSaveListener = listener
        return this
    }

    private fun setup(view: View) {
        saveButton = view.findViewById(R.id.student_form_save_button)
        deleteButton = view.findViewById(R.id.student_form_delete_button)
        cancelButton = view.findViewById(R.id.student_form_cancel_button)

        nameField = view.findViewById(R.id.student_form_name_edit_text)
        idField = view.findViewById(R.id.student_form_id_edit_text)
        phoneField = view.findViewById(R.id.student_form_phone_edit_text)
        addressField = view.findViewById(R.id.student_form_address_edit_text)
        checkBoxField = view.findViewById(R.id.student_form_check_box)
        birthDate = view.findViewById(R.id.student_form_birth_date_edit_text)
        birthTime = view.findViewById(R.id.student_form_birth_time_edit_text)
    }

    private fun initiateForm() {
        val student = Model.shared.get(studentPosition as Int)

        nameField?.setText(student.name)
        idField?.setText(student.id)
        phoneField?.setText(student.phone)
        addressField?.setText(student.address)
        checkBoxField?.isChecked = student.isChecked

        onTimeSet(student.birthHour, student.birthMinute)
        onDateSet(student.birthYear, student.birthMonth, student.birthDay)
    }

    private fun onSaveButton(view: View) {
        val name = nameField?.text.toString()
        val id = idField?.text.toString()
        val phone = phoneField?.text.toString()
        val address = addressField?.text.toString()
        val isChecked = checkBoxField?.isChecked == true
        val avatarUrl = ""

        if (name == "") {
            alert("Please write a Name") { _, _ -> }
        } else if (id == "") {
            alert("Please write a ID") { _, _ -> }
        } else if (phone == "") {
            alert("Please write a Phone") { _, _ -> }
        } else if (address == "") {
            alert("Please write a Address") { _, _ -> }
        } else if (birthDay == null) {
            alert("Please enter a Birth Date") { _, _ -> }
        } else if (birthHour == null) {
            alert("Please enter a Birth Time") { _, _ -> }
        } else {
            val student = Student(
                name = name,
                id = id,
                phone = phone,
                address = address,
                avatarUrl = avatarUrl,
                isChecked = isChecked,
                birthHour = birthHour as Int,
                birthMinute = birthMinute as Int,
                birthDay = birthDay as Int,
                birthMonth = birthMonth as Int,
                birthYear = birthYear as Int
            )

            Model.shared.createOrUpdate(studentPosition, student)
            alert("Saved successfully") { _, _ ->
                if (onSaveListener != null) {
                    onSaveListener?.onSave(view)
                } else {
                    Navigation.findNavController(view).popBackStack()
                }
            }
        }
    }

    private fun onDeleteButton(view: View) {
        Model.shared.remove(studentPosition as Int)
        alert("Deleted successfully") { _, _ ->
            Navigation.findNavController(view).popBackStack(R.id.studentsListFragment, false)
        }
    }

    private fun onCancelButton(view: View) {
        Navigation.findNavController(view).popBackStack()
    }

    private fun alert(message: String, listener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(context)
            .setTitle(message)
            .setPositiveButton("Ok", listener)
            .setCancelable(false)
            .create().show()
    }

    companion object {
        fun newInstance(studentPosition: Int) =
            StudentFormFragment().apply {
                arguments = Bundle().apply {
                    putInt(STUDENT_POSITION, studentPosition)
                }
            }

        fun newInstance() = StudentFormFragment()
    }

    override fun onTimeSet(hourOfDay: Int, minute: Int) {
        birthHour = hourOfDay
        birthMinute = minute
        birthTime?.setText(timeString(hourOfDay, minute))
    }

    override fun onDateSet(year: Int, month: Int, day: Int) {
        birthDay = day
        birthMonth = month
        birthYear = year
        birthDate?.setText(dateString(year, month, day))
    }
}