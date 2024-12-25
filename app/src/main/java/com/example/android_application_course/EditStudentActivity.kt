package com.example.android_application_course

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.android_application_course.model.Model

class EditStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Edit Student"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val saveButton: Button = findViewById(R.id.edit_student_save_button)
        val deleteButton: Button = findViewById(R.id.edit_student_delete_button)
        val cancelButton: Button = findViewById(R.id.edit_student_cancel_button)

        val nameField: EditText = findViewById(R.id.edit_student_name_edit_text)
        val idField: EditText = findViewById(R.id.edit_student_id_edit_text)
        val phoneField: EditText = findViewById(R.id.edit_student_phone_edit_text)
        val addressField: EditText = findViewById(R.id.edit_student_address_edit_text)
        val checkBoxField: CheckBox = findViewById(R.id.edit_student_check_box)

        val extras = intent.extras
        if (extras != null) {
            val index = extras.getInt("studentPosition")

            val student = Model.shared.get(index)

            nameField.setText(student.name)
            idField.setText(student.id)
            phoneField.setText(student.phone)
            addressField.setText(student.address)
            checkBoxField.isChecked = student.isChecked

            cancelButton.setOnClickListener {
                finish()
            }

            saveButton.setOnClickListener {
                val name = nameField.text.toString()
                val id = idField.text.toString()
                val phone = phoneField.text.toString()
                val address = addressField.text.toString()
                val isChecked = checkBoxField.isChecked

                if (name == "") {
                    Toast.makeText(this, "Please write a Name", Toast.LENGTH_SHORT).show()
                } else if (id == "") {
                    Toast.makeText(this, "Please write an ID", Toast.LENGTH_SHORT).show()
                } else if (phone == "") {
                    Toast.makeText(this, "Please write a Phone", Toast.LENGTH_SHORT).show()
                } else if (address == "") {
                    Toast.makeText(this, "Please write an Address", Toast.LENGTH_SHORT).show()
                } else {
                    val student = Model.shared.get(index)
                    student.name = name
                    student.id = id
                    student.phone = phone
                    student.address = address
                    student.isChecked = isChecked

                    Toast.makeText(this, "Edited successfully", Toast.LENGTH_SHORT).show()

                    val intent = Intent()
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }

            deleteButton.setOnClickListener {
                Model.shared.remove(index)

                Toast.makeText(this, "Deleted successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent()
                intent.putExtra("isDeleted", true)
                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}