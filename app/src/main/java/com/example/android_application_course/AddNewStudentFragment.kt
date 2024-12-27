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
import com.example.android_application_course.model.Student

class AddNewStudentFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_new_student, container, false)

        val saveButton: Button = view.findViewById(R.id.add_student_save_button)
        val cancelButton: Button = view.findViewById(R.id.add_student_cancel_button)

        val nameField: EditText = view.findViewById(R.id.add_student_name_edit_text)
        val idField: EditText = view.findViewById(R.id.add_student_id_edit_text)
        val phoneField: EditText = view.findViewById(R.id.add_student_phone_edit_text)
        val addressField: EditText = view.findViewById(R.id.add_student_address_edit_text)
        val checkBoxField: CheckBox = view.findViewById(R.id.add_student_check_box)

        cancelButton.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        saveButton.setOnClickListener {
            val student = Student(
                name = nameField.text.toString(),
                id = idField.text.toString(),
                phone = phoneField.text.toString(),
                address = addressField.text.toString(),
                avatarUrl = "",
                isChecked = checkBoxField.isChecked
            )

            if (student.name == "") {
                Toast.makeText(context, "Please write a Name", Toast.LENGTH_SHORT).show()
            } else if (student.id == "") {
                Toast.makeText(context, "Please write an ID", Toast.LENGTH_SHORT).show()
            } else if (student.phone == "") {
                Toast.makeText(context, "Please write a Phone", Toast.LENGTH_SHORT).show()
            } else if (student.address == "") {
                Toast.makeText(context, "Please write an Address", Toast.LENGTH_SHORT).show()
            } else {
                Model.shared.add(student)
                Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).popBackStack()
            }
        }

        return view
    }
}