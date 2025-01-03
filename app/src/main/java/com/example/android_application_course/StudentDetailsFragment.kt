package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android_application_course.model.Model

class StudentDetailsFragment : Fragment() {
    private var view: View? = null
    private var studentPosition: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_details, container, false)
        this.view = view

        studentPosition = StudentDetailsFragmentArgs.fromBundle(requireArguments()).studentPosition
        val student = Model.shared.get(studentPosition as Int)

        val nameField: TextView = view.findViewById(R.id.student_details_name)
        val idField: TextView = view.findViewById(R.id.student_details_id)
        val phoneField: TextView = view.findViewById(R.id.student_details_phone)
        val addressField: TextView = view.findViewById(R.id.student_details_address)
        val checkBoxField: CheckBox = view.findViewById(R.id.student_details_check_box)
        val checkedField: TextView = view.findViewById(R.id.student_details_checked)
        val birthDateField: TextView = view.findViewById(R.id.student_details_birth_date)
        val birthTimeField: TextView = view.findViewById(R.id.student_details_birth_time)

        nameField.text = "Name: ${student.name}"
        idField.text = "ID: ${student.id}"
        phoneField.text = "Phone: ${student.phone}"
        addressField.text = "Address: ${student.address}"
        birthDateField.text = "Birth Date: ${dateString(student.birthYear, student.birthMonth, student.birthDay)}"
        birthTimeField.text = "Birth Time: ${timeString(student.birthHour, student.birthMinute)}"

        if (student.isChecked) {
            checkBoxField.isChecked = true
            checkedField.text = "Checked"
        } else {
            checkBoxField.isChecked = false
            checkedField.text = "Not Checked"
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit_student_menu) {
            val action = StudentDetailsFragmentDirections.actionStudentDetailsFragmentToEditStudentFragment(studentPosition as Int)
            Navigation.findNavController(view as View).navigate(action)
            return true
        }

        return false
    }
}