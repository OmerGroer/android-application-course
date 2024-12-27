package com.example.android_application_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.android_application_course.model.Model

class EditStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_student, container, false)

        val saveButton: Button = view.findViewById(R.id.edit_student_save_button)
        val deleteButton: Button = view.findViewById(R.id.edit_student_delete_button)
        val cancelButton: Button = view.findViewById(R.id.edit_student_cancel_button)

        val nameField: EditText = view.findViewById(R.id.edit_student_name_edit_text)
        val idField: EditText = view.findViewById(R.id.edit_student_id_edit_text)
        val phoneField: EditText = view.findViewById(R.id.edit_student_phone_edit_text)
        val addressField: EditText = view.findViewById(R.id.edit_student_address_edit_text)
        val checkBoxField: CheckBox = view.findViewById(R.id.edit_student_check_box)

        val studentPosition = EditStudentFragmentArgs.fromBundle(requireArguments()).studentPosition

        val student = Model.shared.get(studentPosition)

        nameField.setText(student.name)
        idField.setText(student.id)
        phoneField.setText(student.phone)
        addressField.setText(student.address)
        checkBoxField.isChecked = student.isChecked

        cancelButton.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            val id = idField.text.toString()
            val phone = phoneField.text.toString()
            val address = addressField.text.toString()
            val isChecked = checkBoxField.isChecked

            if (name == "") {
                Toast.makeText(context, "Please write a Name", Toast.LENGTH_SHORT).show()
            } else if (id == "") {
                Toast.makeText(context, "Please write an ID", Toast.LENGTH_SHORT).show()
            } else if (phone == "") {
                Toast.makeText(context, "Please write a Phone", Toast.LENGTH_SHORT).show()
            } else if (address == "") {
                Toast.makeText(context, "Please write an Address", Toast.LENGTH_SHORT).show()
            } else {
                val student = Model.shared.get(studentPosition)
                student.name = name
                student.id = id
                student.phone = phone
                student.address = address
                student.isChecked = isChecked

                Toast.makeText(context, "Edited successfully", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).popBackStack()
            }
        }

        deleteButton.setOnClickListener {
            Model.shared.remove(studentPosition)

            Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()

            val action = EditStudentFragmentDirections.actionEditStudentFragmentToStudentsListFragment()
            Navigation.findNavController(view).navigate(action)
        }

        return view
    }
}