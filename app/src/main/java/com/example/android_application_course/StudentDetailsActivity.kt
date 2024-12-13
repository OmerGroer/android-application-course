package com.example.android_application_course

import android.os.Bundle
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.android_application_course.model.Model

class StudentDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_student_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        supportActionBar?.title = "Student Details"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val extras = intent.extras
        if (extras != null) {
            val index = extras.getInt("studentPosition")

            val student = Model.shared.students.get(index)

            val nameField: TextView = findViewById(R.id.student_details_name)
            val idField: TextView = findViewById(R.id.student_details_id)
            val phoneField: TextView = findViewById(R.id.student_details_phone)
            val addressField: TextView = findViewById(R.id.student_details_address)
            val checkBoxField: CheckBox = findViewById(R.id.student_details_check_box)
            val checkedField: TextView = findViewById(R.id.student_details_checked)

            nameField.text = "Name: ${student.name}"
            idField.text = "ID: ${student.id}"
            phoneField.text = "Phone: ${student.phone}"
            addressField.text = "Address: ${student.address}"

            if (student.isChecked) {
                checkBoxField.isChecked = true
                checkedField.text = "Checked"
            } else {
                checkBoxField.isChecked = false
                checkedField.text = "Not Checked"
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish();
        return true;
    }
}