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

private const val STUDENT_POSITION = "studentPosition"

class StudentFormFragment : Fragment() {
    private var studentPosition: Int? = null

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

        val saveButton: Button = view.findViewById(R.id.student_form_save_button)
        val deleteButton: Button = view.findViewById(R.id.student_form_delete_button)
        val cancelButton: Button = view.findViewById(R.id.student_form_cancel_button)

        val nameField: EditText = view.findViewById(R.id.student_form_name_edit_text)
        val idField: EditText = view.findViewById(R.id.student_form_id_edit_text)
        val phoneField: EditText = view.findViewById(R.id.student_form_phone_edit_text)
        val addressField: EditText = view.findViewById(R.id.student_form_address_edit_text)
        val checkBoxField: CheckBox = view.findViewById(R.id.student_form_check_box)

        if (studentPosition != null) {
            val student = Model.shared.get(studentPosition as Int)

            nameField.setText(student.name)
            idField.setText(student.id)
            phoneField.setText(student.phone)
            addressField.setText(student.address)
            checkBoxField.isChecked = student.isChecked

            deleteButton.setOnClickListener {
                Model.shared.remove(studentPosition as Int)

                Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()

                Navigation.findNavController(view).popBackStack(R.id.studentsListFragment, false)
            }
        } else {
            deleteButton.visibility = View.GONE
        }

        cancelButton.setOnClickListener {
            Navigation.findNavController(view).popBackStack()
        }

        saveButton.setOnClickListener {
            val name = nameField.text.toString()
            val id = idField.text.toString()
            val phone = phoneField.text.toString()
            val address = addressField.text.toString()
            val isChecked = checkBoxField.isChecked
            val avatarUrl = ""

            if (name == "") {
                Toast.makeText(context, "Please write a Name", Toast.LENGTH_SHORT).show()
            } else if (id == "") {
                Toast.makeText(context, "Please write an ID", Toast.LENGTH_SHORT).show()
            } else if (phone == "") {
                Toast.makeText(context, "Please write a Phone", Toast.LENGTH_SHORT).show()
            } else if (address == "") {
                Toast.makeText(context, "Please write an Address", Toast.LENGTH_SHORT).show()
            } else {
                if (studentPosition != null) {
                    val student = Model.shared.get(studentPosition as Int)
                    student.name = name
                    student.id = id
                    student.phone = phone
                    student.address = address
                    student.isChecked = isChecked

                    Toast.makeText(context, "Edited successfully", Toast.LENGTH_SHORT).show()
                } else {
                    val student = Student(
                        name = name,
                        id = id,
                        phone = phone,
                        address = address,
                        avatarUrl = avatarUrl,
                        isChecked = isChecked
                    )

                    Model.shared.add(student)
                    Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
                }

                Navigation.findNavController(view).popBackStack()
            }
        }

        return view
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
}