package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android_application_course.model.Model

class StudentDetailsFragment : Fragment() {
    private var studentPosition: Int? = null
    private var checkBoxField: CheckBox? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_details, container, false)

        studentPosition = StudentDetailsFragmentArgs.fromBundle(requireArguments()).studentPosition
        val studentPosition = studentPosition as Int
        val student = Model.shared.get(studentPosition)

        val nameField: TextView = view.findViewById(R.id.student_details_name)
        val idField: TextView = view.findViewById(R.id.student_details_id)
        val phoneField: TextView = view.findViewById(R.id.student_details_phone)
        val addressField: TextView = view.findViewById(R.id.student_details_address)
        checkBoxField = view.findViewById(R.id.student_details_check_box)
        val checkedField: TextView = view.findViewById(R.id.student_details_checked)

        nameField.text = "Name: ${student.name}"
        idField.text = "ID: ${student.id}"
        phoneField.text = "Phone: ${student.phone}"
        addressField.text = "Address: ${student.address}"

        if (student.isChecked) {
            checkBoxField?.isChecked = true
            checkedField.text = "Checked"
        } else {
            checkBoxField?.isChecked = false
            checkedField.text = "Not Checked"
        }

        val editButton: Button = view.findViewById(R.id.student_details_edit_button)
        editButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                StudentDetailsFragmentDirections.actionStudentDetailsFragmentToEditStudentFragment(
                    studentPosition
                )
            )
        )

        return view
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        checkBoxField?.isChecked = Model.shared.get(studentPosition as Int).isChecked
    }
}