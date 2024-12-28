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

    var saveButton: Button? = null
    var deleteButton: Button? = null
    var cancelButton: Button? = null
    var nameField: EditText? = null
    var idField: EditText? = null
    var phoneField: EditText? = null
    var addressField: EditText? = null
    var checkBoxField: CheckBox? = null

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

        setup(view)

        if (studentPosition != null) {
            initiateForm()
        } else {
            deleteButton?.visibility = View.GONE
        }

        saveButton?.setOnClickListener(::onSaveButton)
        cancelButton?.setOnClickListener(::onCancelButton)
        deleteButton?.setOnClickListener(::onDeleteButton)

        return view
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
    }

    private fun initiateForm() {
        val student = Model.shared.get(studentPosition as Int)

        nameField?.setText(student.name)
        idField?.setText(student.id)
        phoneField?.setText(student.phone)
        addressField?.setText(student.address)
        checkBoxField?.isChecked = student.isChecked
    }

    private fun onSaveButton(view: View) {
        val name = nameField?.text.toString()
        val id = idField?.text.toString()
        val phone = phoneField?.text.toString()
        val address = addressField?.text.toString()
        val isChecked = checkBoxField?.isChecked == true
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
            val student = Student(
                name = name,
                id = id,
                phone = phone,
                address = address,
                avatarUrl = avatarUrl,
                isChecked = isChecked
            )

            Model.shared.createOrUpdate(studentPosition, student)
            Toast.makeText(context, "Saved successfully", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(view).popBackStack()
        }
    }

    private fun onDeleteButton(view: View) {
        Model.shared.remove(studentPosition as Int)
        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show()
        Navigation.findNavController(view).popBackStack(R.id.studentsListFragment, false)
    }

    private fun onCancelButton(view: View) {
        Navigation.findNavController(view).popBackStack()
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