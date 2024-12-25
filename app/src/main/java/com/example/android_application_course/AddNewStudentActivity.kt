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
import com.example.android_application_course.model.Student

class AddNewStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_new_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Add New Student"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val saveButton: Button = findViewById(R.id.add_student_save_button)
        val cancelButton: Button = findViewById(R.id.add_student_cancel_button)

        val nameField: EditText = findViewById(R.id.add_student_name_edit_text)
        val idField: EditText = findViewById(R.id.add_student_id_edit_text)
        val phoneField: EditText = findViewById(R.id.add_student_phone_edit_text)
        val addressField: EditText = findViewById(R.id.add_student_address_edit_text)
        val checkBoxField: CheckBox = findViewById(R.id.add_student_check_box)

        cancelButton.setOnClickListener {
            finish()
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
                Toast.makeText(this, "Please write a Name", Toast.LENGTH_SHORT).show()
            } else if (student.id == "") {
                Toast.makeText(this, "Please write an ID", Toast.LENGTH_SHORT).show()
            } else if (student.phone == "") {
                Toast.makeText(this, "Please write a Phone", Toast.LENGTH_SHORT).show()
            } else if (student.address == "") {
                Toast.makeText(this, "Please write an Address", Toast.LENGTH_SHORT).show()
            } else {
                Model.shared.add(student)
                Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()

                val intent = Intent()
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